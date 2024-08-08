package com.hulimova.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public interface ConfigProvider {

    Config config = readConfig();

    String PROTON_AUTH_PAGE_URL = config.getString("account_1.auth_page_url");

    String PROTON_SERVICE_URL = config.getString("account_1.service_url");

    String PROTON_MAIL_LOGIN_FIRST_ACCOUNT = config.getString("account_1.email");

    String PROTON_MAIL_PASSWORD_FIRST_ACCOUNT = config.getString("account_1.password");

    String PROTON_MAIL_LOGIN_SECOND_ACCOUNT = config.getString("account_2.email");

    String PROTON_MAIL_PASSWORD_SECOND_ACCOUNT = config.getString("account_2.password");

    String PASSWORD_IS_INCORRECT_MESSAGE = config.getString("account_1.password_is_incorrect_message");

    String EMAIL_DOES_NOT_EXIST_MESSAGE = config.getString("account_1.email_does_not_exist_message");

    String FIELD_IS_REQUIRED_MESSAGE = config.getString("account_1.field_is_required_message");

    String TEXT_FOR_MAIL = config.getString("text_for_mail");

    String SUBJECT_FOR_EMAIL = config.getString("subject_for_email");

    String BROWSER = config.getString("browser");

    static Config readConfig() {
        return ConfigFactory.systemProperties().hasPath("testProfile")
                ? ConfigFactory.load(ConfigFactory.systemProperties().getString("testProfile"))
                : ConfigFactory.load("application.conf");
    }
}
