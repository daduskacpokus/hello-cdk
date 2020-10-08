package com.myorg;

import software.amazon.awscdk.core.App;

public final class HelloCdkApp {
    public static void main(final String[] args) {
        App app = new App();

        new HelloCdkStack(app, "HelloCdkStack");

        app.synth();
    }
}
