package com.salmon.test.framework.helpers;

import com.salmon.test.framework.PageObject;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ScreenshotHook extends PageObject {

    protected static final Logger LOG = LoggerFactory.getLogger(ScreenshotHook.class);

    @After
    public void embedScreenshot(Scenario scenario) {
        try {
            Map<String, Object> screenShots = Screenshots.getScreenShotsForCurrentTest();
            for (Map.Entry<String, Object> screenShot : screenShots.entrySet()) {
                scenario.write(screenShot.getKey());
                scenario.embed((byte[]) screenShot.getValue(), "image/png");
            }

            Screenshots.tidyUpAfterTestRun();

            if (scenario.isFailed()) {
                scenario.write(getWebDriver().getCurrentUrl());
                byte[] screenShot = ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.embed(screenShot, "image/png");
            }

        } catch (WebDriverException wde) {
            LOG.error(wde.getMessage());
        } catch (ClassCastException wde) {
            LOG.error(wde.getMessage());
        }
        finally {
            getWebDriver().switchTo().defaultContent();
        }
    }
}