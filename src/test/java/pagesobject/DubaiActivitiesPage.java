package pagesobject;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import org.testng.Assert;
import util.Screenshot;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object for the Dubai Activities page (dev.raynatours.com/dubai-activities).
 * Provides methods to navigate, click "Load more" multiple times, verify visible images,
 * and perform simple spelling heuristics on visible titles/text.
 */
public class DubaiActivitiesPage {
    private final Page page;
    private final String url = "https://dev.raynatours.com/dubai-activities";

    public DubaiActivitiesPage(Page page) {
        this.page = page;
    }

    public void navigateToPage() {
        page.navigate(url);
        page.waitForLoadState();
        // Wait for meaningful content to appear (headings or activity cards). Retry up to ~15s.
        try {
            page.waitForSelector("h1, h2, .activity-card, .tour-card, .listing-item", new Page.WaitForSelectorOptions().setTimeout(15000));
        } catch (Exception ignored) {
            // ignore - subsequent checks will handle missing content and generate useful screenshots
            page.waitForTimeout(1000);
        }
    }

    /**
     * Clicks load more multiple times until maxClicks or until no button found.
     */
    public void clickLoadMoreMultipleTimes(int maxClicks) {
        for (int i = 0; i < maxClicks; i++) {
            try {
                // scroll a bit to expose the button
                page.evaluate("() => window.scrollBy(0, window.innerHeight)");
                page.waitForTimeout(300);

                Locator btn = page.locator("text=Load more");
                if (btn.count() == 0) btn = page.locator("text=Load More");
                if (btn.count() == 0) btn = page.locator("button:has-text(\"Load\")");
                if (btn.count() == 0) btn = page.locator(".load-more, .loadmore, .btn-load-more");

                if (btn.count() == 0) {
                    // no button found - stop early
                    break;
                }

                // wait for the first button to be enabled/visible
                Locator first = btn.first();
                try { first.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000)); } catch (Exception e) {}
                first.scrollIntoViewIfNeeded();
                first.click();
                // wait for content to load
                page.waitForTimeout(800);

            } catch (Exception e) {
                // if anything goes wrong, stop to avoid infinite loop
                break;
            }
        }
    }

    /**
     * Verify visible images are loaded (naturalWidth > 0) and have non-empty src.
     * Scrolls through the page to trigger lazy-load before checking.
     */
    public void verifyVisibleImagesLoaded(String testName) {
        // Scroll through page to trigger lazy-loaded images
        page.evaluate("() => window.scrollTo(0, document.body.scrollHeight)");
        page.waitForTimeout(2000);
        page.evaluate("() => window.scrollTo(0, 0)");
        page.waitForTimeout(1000);

        // Wait for images to finish loading (up to 10s)
        page.evaluate(
            "() => new Promise(resolve => {" +
            "  const imgs = Array.from(document.images);" +
            "  if (imgs.every(i => i.complete)) { resolve(); return; }" +
            "  let count = 0;" +
            "  imgs.forEach(i => { if (!i.complete) i.addEventListener('load', () => { count++; if (count === imgs.filter(x=>!x.complete).length) resolve(); }); });" +
            "  setTimeout(resolve, 10000);" +
            "})"
        );

        @SuppressWarnings("unchecked")
        List<Object> images = (List<Object>) page.evaluate(
            "() => Array.from(document.images)" +
            "  .filter(i => { const r = i.getBoundingClientRect(); return i.src && i.src !== ''; })" +
            "  .map(i => ({ src: i.currentSrc || i.src || '', naturalWidth: i.naturalWidth, complete: i.complete }))"
        );

        List<String> broken = new ArrayList<>();
        for (Object o : images) {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> m = (java.util.Map<String, Object>) o;
            String src = m.getOrDefault("src", "").toString();
            Number nw = (Number) m.getOrDefault("naturalWidth", 0);
            Boolean complete = (Boolean) m.getOrDefault("complete", false);

            if (src.isEmpty()) continue; // skip images with no src
            if (nw == null || nw.intValue() == 0 || Boolean.FALSE.equals(complete)) {
                broken.add("NOT LOADED: " + src.substring(src.lastIndexOf('/') + 1));
            }
        }

        if (!broken.isEmpty()) {
            String path = "";
            try { path = Screenshot.takeScreenshot(page, testName + "_broken_images"); } catch (Exception ignored) {}
            Assert.fail("Broken/unloaded images (" + broken.size() + "): " + broken +
                        (path.isEmpty() ? "" : " | Screenshot: " + path));
        }
    }

    /**
     * Collect visible titles using common selectors and heuristics.
     */
    public List<String> collectVisibleTitles() {
        List<String> res = new ArrayList<>();
        // headings
        try {
            Locator h = page.locator("h1, h2, h3");
            int c = (int) h.count();
            for (int i = 0; i < c; i++) {
                Locator one = h.nth(i);
                if (!one.isVisible()) continue;
                String t = one.textContent();
                if (t != null) t = t.trim();
                if (t != null && !t.isEmpty() && !res.contains(t)) res.add(t);
            }
        } catch (Exception ignored) {}

        // common card title classes
        try {
            Locator ct = page.locator(".card-title, .title, .activity-title, .product-title, .tour-title, .listing-title");
            int cc = (int) ct.count();
            for (int i = 0; i < cc; i++) {
                Locator one = ct.nth(i);
                if (!one.isVisible()) continue;
                String t = one.textContent();
                if (t != null) t = t.trim();
                if (t != null && !t.isEmpty() && !res.contains(t)) res.add(t);
            }
        } catch (Exception ignored) {}

        return res;
    }

    /**
     * Verify that expected titles roughly match collected titles (case-insensitive exact match).
     */
    public void verifyTitlesPresent(List<String> expectedTitles) {
        List<String> actual = collectVisibleTitles();
        for (String e : expectedTitles) {
            boolean ok = false;
            for (String a : actual) {
                if (a.equalsIgnoreCase(e.trim())) { ok = true; break; }
            }
            if (!ok) {
                String path = "";
                try { path = Screenshot.takeScreenshot(page, "title_missing"); } catch (Exception ignored) {}
                Assert.fail("Expected title not found: '" + e + "'. Actual: " + actual + (path.isEmpty()?"":" Screenshot: " + path));
            }
        }
    }
}