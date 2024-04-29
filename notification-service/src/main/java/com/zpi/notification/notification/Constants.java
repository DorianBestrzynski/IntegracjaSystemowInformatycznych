package com.zpi.notification.notification;

public class Constants {

    public static final String USER_ADDED_TO_GROUP_TITLE = "Around The World - New user joined your group!";

    public static final String USER_ADDED_TO_GROUP_HTML_BODY = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 0;
                    padding: 20px;
                    background-color: #f4f4f4;
                    color: #333;
                }
                .container {
                    background-color: #ffffff;
                    width: 100%;
                    max-width: 600px;
                    margin: 0 auto;
                    padding: 20px;
                    box-shadow: 0 0 10px rgba(0,0,0,0.1);
                }
                .content {
                    font-size: 16px;
                    color: #555;
                    text-align: left;
                }
            </style>
            </head>
            <body>
            <div class="container">
                <div class="content">
                    Dear Coordinator,
                    <br><br>
                    Please be informed that a new member, <strong>{{ name }} {{ surname }}</strong>, has joined your trip group.
                    <br><br>
                    Best Regards,
                    <br>
                    Around The World Team
                </div>
            </div>
            </body>
            </html>""";

    public static final String USER_REGISTERED_TITLE = "Around The World - Welcome!";

    public static final String USER_REGISTERED_HTML_BODY = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 0;
                    padding: 20px;
                    background-color: #f4f4f4;
                    color: #333;
                }
                .container {
                    background-color: #ffffff;
                    width: 100%;
                    max-width: 600px;
                    margin: 0 auto;
                    padding: 20px;
                    box-shadow: 0 0 10px rgba(0,0,0,0.1);
                }
                .content {
                    font-size: 16px;
                    color: #555;
                    text-align: left;
                }
            </style>
            </head>
            <body>
            <div class="container">
                <div class="content">
                    Hi {{ name }},
                    <br><br>
                    We're excited to have you on board! Thanks for registering with Around The World.
                    Enjoy your journey with Around The World!
                    <br><br>
                    Cheers,
                    <br>
                    The Around The World Team
                            </div>
            </div>
            </body>
            </html>""";

    public static final String NEW_EXPENDITURE_TITLE = "Around The World - New expenditure created!";

    public static final String NEW_EXPENDITURE_HTML_BODY = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 0;
                    padding: 20px;
                    background-color: #f4f4f4;
                    color: #333;
                }
                .container {
                    background-color: #ffffff;
                    width: 100%;
                    max-width: 600px;
                    margin: 0 auto;
                    padding: 20px;
                    box-shadow: 0 0 10px rgba(0,0,0,0.1);
                }
                .content {
                    font-size: 16px;
                    color: #555;
                    text-align: left;
                }
            </style>
            </head>
            <body>
            <div class="container">
                <div class="content">
                    Hi {{ debtorName }},
                   <br><br>
                    Please be informed that <strong>{{ name }} {{ surname }}</strong> has created a new expense which includes you.
                    The expense is titled <strong>{{ title }}</strong> and was created on <strong>{{ date }}</strong> for <strong>{{ price }} EUR</strong>.
                    <br><br>
                    You can view this expense and any details related to it in the app.
                    <br><br>
                    Cheers,
                    <br>
                    The Around The World Team
                    </div>
            </div>
            </body>
            </html>""";

    public static final String RESOLVED_EXPENDITURE_TITLE = "Around The World - Expenditure resolved!";

    public static final String RESOLVED_EXPENDITURE_HTML_BODY = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 0;
                    padding: 20px;
                    background-color: #f4f4f4;
                    color: #333;
                }
                .container {
                    background-color: #ffffff;
                    width: 100%;
                    max-width: 600px;
                    margin: 0 auto;
                    padding: 20px;
                    box-shadow: 0 0 10px rgba(0,0,0,0.1);
                }
                .content {
                    font-size: 16px;
                    color: #555;
                    text-align: left;
                }
            </style>
            </head>
            <body>
            <div class="container">
                <div class="content">
                    Hi {{ debteeName }},
                    <br><br>
                    We are pleased to inform you that <strong>{{ name }} {{ surname }}</strong> has marked your expense request as resolved on <strong>{{ date }}</strong> for <strong>{{ price }} EUR</strong>.
                    <br><br>
                    Cheers,
                    <br>
                    The Around The World Team
                    </div>
            </div>
            </body>
            </html>""";
}
