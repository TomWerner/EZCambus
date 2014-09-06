package com.wernerapps.ezbongo;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;


@ReportsCrashes(
        formKey = "",
        formUri = "http://wernerapps.iriscouch.com/acra-ezcambus/_design/acra-storage/_update/report",
        reportType = org.acra.sender.HttpSender.Type.JSON,
        httpMethod = org.acra.sender.HttpSender.Method.PUT,
		formUriBasicAuthLogin = "bugreporter",
		formUriBasicAuthPassword = "password",
        // Your usual ACRA configuration
		mode = ReportingInteractionMode.DIALOG,
        resToastText = R.string.crashDialogTitle, // optional, displayed as soon as the crash occurs, before collecting data which can take a few seconds
        resDialogText = R.string.crashDialogText,
        resDialogIcon = android.R.drawable.ic_dialog_info, //optional. default is a warning sign
        resDialogTitle = R.string.crashDialogTitle, // optional. default is your application name
        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, // optional. when defined, adds a user text field input with this text resource as a label
        resDialogOkToast = R.string.crash_dialog_ok_toast // optional. displays a Toast message when the user accepts to send a report.
        )
public class EzCambus extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();
	}
}