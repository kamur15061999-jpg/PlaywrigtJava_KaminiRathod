package pagesobject;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import util.Screenshot;

import java.util.ArrayList;
import java.util.List;

public class ContentCheckPage {

    private final Page page;

    public ContentCheckPage(Page page) {
        this.page = page;
    }

    // Step 1: Open page
    public void navigate(String url) {
        page.navigate(url);
        page.waitForLoadState();
        page.waitForTimeout(1500);
    }

    // Step 2: Click Load More until button disappears (max clicks)
    public void clickLoadMoreUntilEnd(int maxClicks) {
        for (int i = 0; i < maxClicks; i++) {
            // scroll down to expose Load More button
            page.evaluate("() => window.scrollTo(0, document.body.scrollHeight)");
            page.waitForTimeout(1000);

            Locator btn = page.locator(
                "button:has-text('Load more'), " +
                "button:has-text('Load More'), " +
                "a:has-text('Load more'), " +
                "a:has-text('Load More')"
            );

            if (btn.count() == 0) {
                System.out.println("No Load More button found. Stopped at click " + i);
                break;
            }

            try {
                btn.first().scrollIntoViewIfNeeded();
                btn.first().click();
                page.waitForTimeout(1500);
                System.out.println("Load More clicked: " + (i + 1));
            } catch (Exception e) {
                System.out.println("Load More not clickable. Stopped at click " + i);
                break;
            }
        }

        // Step 3: Scroll full page to trigger all lazy-load images
        page.evaluate("() => window.scrollTo(0, document.body.scrollHeight)");
        page.waitForTimeout(2000);
        page.evaluate("() => window.scrollTo(0, 0)");
        page.waitForTimeout(500);
    }

    // Step 4: Check all images — return broken ones
    @SuppressWarnings("unchecked")
    public List<String> getBrokenImages() {
        List<String> broken = new ArrayList<>();
        try {
            List<Object> results = (List<Object>) page.evaluate(
                "() => Array.from(document.images).map(img => ({" +
                "  src: img.currentSrc || img.src || ''," +
                "  alt: img.alt || ''," +
                "  naturalWidth: img.naturalWidth," +
                "  complete: img.complete" +
                "}))"
            );

            for (Object o : results) {
                java.util.Map<String, Object> m = (java.util.Map<String, Object>) o;
                String src      = m.getOrDefault("src", "").toString().trim();
                String alt      = m.getOrDefault("alt", "").toString().trim();
                Number nw       = (Number) m.getOrDefault("naturalWidth", 0);
                Boolean complete = (Boolean) m.getOrDefault("complete", false);

                if (src.isEmpty()) {
                    broken.add("Empty src (alt='" + alt + "')");
                } else if (nw == null || nw.intValue() == 0 || Boolean.FALSE.equals(complete)) {
                    String shortSrc = src.length() > 80
                            ? "..." + src.substring(src.length() - 80) : src;
                    broken.add("Not loaded [nw=" + (nw == null ? "null" : nw) + "]: " + shortSrc);
                }
            }
        } catch (Exception e) {
            broken.add("Error checking images: " + e.getMessage());
        }
        return broken;
    }

    // Step 5: Take screenshot
    public String takeScreenshot(String name) {
        try {
            return Screenshot.takeScreenshot(page, name);
        } catch (Exception e) {
            return "screenshot-failed";
        }
    }
}
