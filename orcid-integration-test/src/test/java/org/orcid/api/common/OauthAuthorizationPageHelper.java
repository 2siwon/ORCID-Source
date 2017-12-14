/**
 * =============================================================================
 *
 * ORCID (R) Open Source
 * http://orcid.org
 *
 * Copyright (c) 2012-2014 ORCID, Inc.
 * Licensed under an MIT-Style License (MIT)
 * http://orcid.org/open-source-license
 *
 * This copyright and license information (including a link to the full license)
 * shall be included in its entirety in all copies or substantial portion of
 * the software.
 *
 * =============================================================================
 */
package org.orcid.api.common;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.orcid.integration.blackbox.api.BBBUtil;
import org.orcid.pojo.ajaxForm.PojoUtil;

public class OauthAuthorizationPageHelper {

    public static final String authorizationScreenUrl = "%s/oauth/authorize?client_id=%s&response_type=code&scope=%s&redirect_uri=%s";
    public static final String authorizationScreenUrlWithCode = "%s/oauth/authorize?client_id=%s&response_type=%s&scope=%s&redirect_uri=%s";
    public static final String RESPONSE_TYPE_CODE = "code";
    
    public static String loginAndAuthorize(String baseUrl, String clientId, String redirectUri, String scopes, String stateParam, String userId, String password, boolean longLife, WebDriver webDriver) {
        return loginAndAuthorize(baseUrl, clientId,  redirectUri, scopes, stateParam, userId, password, longLife, null, webDriver,RESPONSE_TYPE_CODE);
    }
    
    public static String loginAndAuthorize(String baseUrl, String clientId, String redirectUri, String scopes, String stateParam, String userId, String password, boolean longLife, Map<String,String> params,WebDriver webDriver, String responseType) {
        String formattedAuthorizationScreen = String.format(authorizationScreenUrlWithCode, baseUrl, clientId, responseType, scopes, redirectUri);
        if(!PojoUtil.isEmpty(stateParam)) {
            formattedAuthorizationScreen += "&state=" + stateParam;
        }
        if (params != null){
            for (String key : params.keySet()){
                formattedAuthorizationScreen += "&"+key+"=" + params.get(key);
            }
        }
        
        By userIdElementLocator = By.id("userId");
        webDriver.get(formattedAuthorizationScreen);
        (new WebDriverWait(webDriver, BBBUtil.TIMEOUT_SECONDS, BBBUtil.SLEEP_MILLISECONDS)).until(BBBUtil.documentReady());
        (new WebDriverWait(webDriver, BBBUtil.TIMEOUT_SECONDS, BBBUtil.SLEEP_MILLISECONDS)).until(BBBUtil.angularHasFinishedProcessing());        
        (new WebDriverWait(webDriver, BBBUtil.TIMEOUT_SECONDS)).until(ExpectedConditions.presenceOfElementLocated(userIdElementLocator));        

        WebElement userIdElement = webDriver.findElement(userIdElementLocator);
        userIdElement.sendKeys(userId);
        (new WebDriverWait(webDriver, BBBUtil.TIMEOUT_SECONDS, BBBUtil.SLEEP_MILLISECONDS)).until(BBBUtil.angularHasFinishedProcessing());
        WebElement passwordElement = webDriver.findElement(By.id("password"));
        passwordElement.sendKeys(password);
        (new WebDriverWait(webDriver, BBBUtil.TIMEOUT_SECONDS, BBBUtil.SLEEP_MILLISECONDS)).until(BBBUtil.angularHasFinishedProcessing());
        
        (new WebDriverWait(webDriver, BBBUtil.TIMEOUT_SECONDS)).until(ExpectedConditions.visibilityOfElementLocated(By.id("form-sign-in-button")));
        
        webDriver.findElement(By.id("form-sign-in-button")).click();

        (new WebDriverWait(webDriver, BBBUtil.TIMEOUT_SECONDS, BBBUtil.SLEEP_MILLISECONDS)).until(BBBUtil.documentReady());        
       
        try {
            BBBUtil.extremeWaitFor(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(text(),'has asked for the following access to your ORCID Record')]")), webDriver);
            if (longLife == false) {
                //disablePersistentToken
                WebElement persistentElement = webDriver.findElement(By.id("enablePersistentToken"));
                if(persistentElement.isDisplayed()) {
                    if (persistentElement.isSelected()) {
                        persistentElement.click();
                        (new WebDriverWait(webDriver, BBBUtil.TIMEOUT_SECONDS, BBBUtil.SLEEP_MILLISECONDS)).until(BBBUtil.angularHasFinishedProcessing());
                    }
                }            
            }
            (new WebDriverWait(webDriver, BBBUtil.TIMEOUT_SECONDS, BBBUtil.SLEEP_MILLISECONDS)).until(BBBUtil.angularHasFinishedProcessing());
            By authorizeElementLocator = By.id("authorize");
            (new WebDriverWait(webDriver, BBBUtil.TIMEOUT_SECONDS)).until(ExpectedConditions.visibilityOfElementLocated(authorizeElementLocator));
            WebElement authorizeButton = webDriver.findElement(By.id("authorize"));
            authorizeButton.click();
            (new WebDriverWait(webDriver, BBBUtil.TIMEOUT_SECONDS)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.getTitle().equals("ORCID Playground");
                }
            });
        } catch(TimeoutException e) {
           //It might be the case that we are already in the ORCID Playground page, so, lets check for that case
           (new WebDriverWait(webDriver, BBBUtil.TIMEOUT_SECONDS)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.getTitle().equals("ORCID Playground");
                }
            });
        } 
        String currentUrl = webDriver.getCurrentUrl();
        return currentUrl;
    }
    
    public static String authorizeOnAlreadyLoggedInUser(final WebDriver loggedInDriver, String baseUrl, String clientId, String redirectUri, String scopes, String stateParam) {
        String formattedAuthorizationScreen = String.format(authorizationScreenUrl, baseUrl, clientId,scopes, redirectUri);
        
        if(!PojoUtil.isEmpty(stateParam)) {
            formattedAuthorizationScreen += "&state=" + stateParam;
        }
        
        loggedInDriver.get(formattedAuthorizationScreen);
        
        try {
            BBBUtil.extremeWaitFor(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(),'has asked for the following access to your ORCID Record')]")), loggedInDriver);
            By authorizeElementLocator = By.id("authorize");
            (new WebDriverWait(loggedInDriver, BBBUtil.TIMEOUT_SECONDS)).until(ExpectedConditions.presenceOfElementLocated(authorizeElementLocator));
            WebElement authorizeButton = loggedInDriver.findElement(By.id("authorize"));
            authorizeButton.click();
            (new WebDriverWait(loggedInDriver, BBBUtil.TIMEOUT_SECONDS)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.getTitle().equals("ORCID Playground");
                }
            });
        } catch(TimeoutException e) {
           //It might be the case that we are already in the ORCID Playground page, so, lets check for that case
           (new WebDriverWait(loggedInDriver, BBBUtil.TIMEOUT_SECONDS)).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.getTitle().equals("ORCID Playground");
                }
            });
        } 

        String result = loggedInDriver.getCurrentUrl();        
        return result;
    }
        
   public static void clickAuthorizeOnAuthorizeScreen(final WebDriver webDriver, boolean longLife) {
        if (webDriver.getTitle().equals("ORCID Playground")){
            return;
        } else {
                try {
                    BBBUtil.extremeWaitFor(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(),'has asked for the following access to your ORCID Record')]")), webDriver);
                    if (longLife == false) {
                        //disablePersistentToken
                        WebElement persistentElement = webDriver.findElement(By.id("enablePersistentToken"));
                        if(persistentElement.isDisplayed()) {
                            if (persistentElement.isSelected()) {
                                persistentElement.click();
                            }
                        }            
                    }
                    WebElement authorizeButton = webDriver.findElement(By.id("authorize"));
                    authorizeButton.click();
                } catch(TimeoutException e) {
                    //It might be the case that we are already in the ORCID Playground page, so, lets check for that case
                    (new WebDriverWait(webDriver, BBBUtil.TIMEOUT_SECONDS)).until(new ExpectedCondition<Boolean>() {
                        public Boolean apply(WebDriver d) {
                            return d.getTitle().equals("ORCID Playground");
                        }
                    });
                }
                
            }
        }
}
