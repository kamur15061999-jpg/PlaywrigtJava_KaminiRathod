package pagesobject;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;
import util.Screenshot;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.nio.file.Paths;

/**
 * ExamplePage implements a simplified page object based on the user's Playwright script.
 * It provides navigation, language/currency selection, searching, filter/sort operations,
 * category clicks, and a robust "load more" routine.
 */
public class ExamplePage {
    private final Page page;
    private final String baseUrl = "https://dev.raynatours.com/";

    public ExamplePage(Page page) {
        this.page = page;
    }

    public void navigateHome() {
        page.navigate(baseUrl);
        page.waitForLoadState();
        try { page.waitForSelector("body", new Page.WaitForSelectorOptions().setTimeout(10000)); } catch (Exception ignored) {}
    }

    public void openActivities() {
        // Click Activities link - try multiple role/text variants
        try {
            page.getByRole(com.microsoft.playwright.options.AriaRole.LINK, new Page.GetByRoleOptions().setName("Activities Activities")).click();
        } catch (Exception e) {
            // fallback: click any link that contains "Activities"
            Locator l = page.locator("a:has-text('Activities')");
            if (l.count() > 0) l.first().click();
        }
        page.waitForLoadState();
    }

    public void openDubaiThingsToDo() {
        try {
            page.getByRole(com.microsoft.playwright.options.AriaRole.LINK, new Page.GetByRoleOptions().setName("Things to do in Dubai United")).click();
        } catch (Exception e) {
            Locator l = page.locator("a:has-text('Things to do in Dubai')");
            if (l.count() > 0) l.first().click();
        }
        page.waitForLoadState();
    }

    public void setCurrencyToAED() {
        try {
            page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName("English / INR")).click();
            page.getByText("Arab Emirate Dirham").click();
        } catch (Exception ignored) {
            // ignore if not present
        }
    }

    public void searchFor(String term) {
        try {
            page.getByTestId("BaseTextInput.input").click();
            page.getByTestId("BaseTextInput.input").fill(term);
            // click outside to trigger suggestions/blur
            try { page.getByTestId("Dialog.div").click(); } catch (Exception ignored) {}
        } catch (Exception ignored) {}
    }

    public void toggleMenuIcon() {
        try { page.locator(".w-6.h-6.stroke-gray-800").click(); } catch (Exception ignored) {}
    }

    public void openSortAndFilter() {
        try { page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sort By")).click(); } catch (Exception ignored) {}
        try { page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Filter By")).click(); } catch (Exception ignored) {}
    }

    public void applyFilterByName(String name) {
        try {
            page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName(name)).click();
            page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Apply")).click();
        } catch (Exception ignored) {}
    }

    public void applySort(String name) {
        try {
            page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sort By")).click();
            page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName(name)).click();
            page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Apply")).click();
        } catch (Exception ignored) {}
    }

    public void clickCategory(String name) {
        try {
            page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName(name)).click();
        } catch (Exception e) {
            Locator l = page.locator("button:has-text('" + name + "')");
            if (l.count() > 0) {
                l.first().click();
            }
        }
    }

    public void clickLoadMoreMultipleTimes(int times) {
        for (int i = 0; i < times; i++) {
            try {
                page.evaluate("() => window.scrollBy(0, window.innerHeight)");
                page.waitForTimeout(300);
                Locator btn = page.locator("text=Load more");
                if (btn.count() == 0) btn = page.locator("text=Load More");
                if (btn.count() == 0) btn = page.locator("button:has-text(\"Load\")");
                if (btn.count() == 0) btn = page.locator(".load-more, .loadmore, .btn-load-more");
                if (btn.count() == 0) break;
                Locator first = btn.first();
                try { first.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000)); } catch (Exception ignored) {}
                first.scrollIntoViewIfNeeded();
                first.click();
                page.waitForTimeout(800);
            } catch (Exception e) {
                break;
            }
        }
    }

    public List<String> collectVisibleTitles() {
        List<String> res = new ArrayList<>();
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

    public void verifyVisibleImagesLoaded(String testName) {
        Object result = page.evaluate(
                "() => { try { const imgs = Array.from(document.images || []); \n" +
                        " return imgs.filter(i => { try { const r = i.getBoundingClientRect(); const visible = r.bottom>0 && r.top<window.innerHeight && r.right>0 && r.left<window.innerWidth; return visible; } catch(e){return false;} }).map(i => ({src:i.currentSrc||i.src||'', naturalWidth: i.naturalWidth, complete: i.complete})); } catch(e){ return []; } }"
        );

        @SuppressWarnings("unchecked")
        List<Object> images = (List<Object>) result;
        List<String> broken = new ArrayList<>();
        int idx = 0;
        for (Object o : images) {
            idx++;
            try {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> m = (java.util.Map<String, Object>) o;
                String src = m.getOrDefault("src", "").toString();
                Number nw = (Number) m.getOrDefault("naturalWidth", 0);
                Boolean complete = (Boolean) m.getOrDefault("complete", false);
                if (src == null || src.trim().isEmpty()) {
                    broken.add("img#" + idx + " empty src");
                    continue;
                }
                if (nw == null || nw.intValue() == 0 || !complete) {
                    broken.add("img[src=" + src + "] not loaded nw=" + (nw==null?"null":nw));
                }
            } catch (Exception ex) {
                broken.add("img#" + idx + " error: " + ex.getMessage());
            }
        }

        if (!broken.isEmpty()) {
            // Try to capture element-level screenshots for each broken image when possible
            int bIdx = 0;
            for (String b : broken) {
                bIdx++;
                try {
                    String src = null;
                    if (b.startsWith("img[src=")) {
                        int start = b.indexOf("img[src=") + "img[src=".length();
                        int end = b.indexOf("]", start);
                        if (end > start) src = b.substring(start, end);
                    }
                    if (src != null && !src.trim().isEmpty()) {
                        // use a contains match to locate the image element on the page
                        Locator imgL = page.locator("img[src*='" + src.replace("'", "\\'") + "']");
                        if (imgL.count() > 0) {
                            try {
                                String p = "test-output/screenshots/" + testName + "_broken_img_" + bIdx + "_" + System.currentTimeMillis() + ".jpg";
                                imgL.first().screenshot(new com.microsoft.playwright.Locator.ScreenshotOptions().setPath(Paths.get(p)));
                            } catch (Exception ignored) {
                                // element screenshot failed; fall through to page screenshot
                            }
                        }
                    }
                } catch (Exception ignored) {}
            }

            String path = "";
            try { path = Screenshot.takeScreenshot(page, testName + "_broken_images"); } catch (Exception ignored) {}
            Assert.fail("Broken or missing images: " + broken + (path.isEmpty()?"":" Screenshot: " + path));
        }
    }

    public List<String> findBrokenVisibleImages(String testName) {
        List<String> broken = new ArrayList<>();
        Object result = page.evaluate(
                "() => { try { const imgs = Array.from(document.images || []); \n" +
                        " return imgs.filter(i => { try { const r = i.getBoundingClientRect(); const visible = r.bottom>0 && r.top<window.innerHeight && r.right>0 && r.left<window.innerWidth; return visible; } catch(e){return false;} }).map(i => ({src:i.currentSrc||i.src||'', naturalWidth: i.naturalWidth, complete: i.complete})); } catch(e){ return []; } }"
        );

        @SuppressWarnings("unchecked")
        List<Object> images = (List<Object>) result;
        int idx = 0;
        for (Object o : images) {
            idx++;
            try {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> m = (java.util.Map<String, Object>) o;
                String src = m.getOrDefault("src", "").toString();
                Number nw = (Number) m.getOrDefault("naturalWidth", 0);
                Boolean complete = (Boolean) m.getOrDefault("complete", false);
                if (src == null || src.trim().isEmpty()) {
                    broken.add("img#" + idx + " empty src");
                    continue;
                }
                if (nw == null || nw.intValue() == 0 || !complete) {
                    broken.add("img[src=" + src + "] not loaded nw=" + (nw==null?"null":nw));
                }
            } catch (Exception ex) {
                broken.add("img#" + idx + " error: " + ex.getMessage());
            }
        }

        if (!broken.isEmpty()) {
            // element-level screenshots
            int bIdx = 0;
            for (String b : broken) {
                bIdx++;
                try {
                    String src = null;
                    if (b.startsWith("img[src=")) {
                        int start = b.indexOf("img[src=") + "img[src=".length();
                        int end = b.indexOf("]", start);
                        if (end > start) src = b.substring(start, end);
                    }
                    if (src != null && !src.trim().isEmpty()) {
                        Locator imgL = page.locator("img[src*='" + src.replace("'", "\\'") + "']");
                        if (imgL.count() > 0) {
                            try {
                                String p = "test-output/screenshots/" + testName + "_broken_img_" + bIdx + "_" + System.currentTimeMillis() + ".jpg";
                                imgL.first().screenshot(new com.microsoft.playwright.Locator.ScreenshotOptions().setPath(Paths.get(p)));
                            } catch (Exception ignored) {}
                        }
                    }
                } catch (Exception ignored) {}
            }
        }

        return broken;
    }
}