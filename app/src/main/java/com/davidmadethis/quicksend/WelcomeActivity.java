package com.davidmadethis.quicksend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeConfiguration;

public class WelcomeActivity extends com.stephentuso.welcome.WelcomeActivity {

    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(R.color.colorPrimaryDark)
                .page(new BasicPage(R.drawable.ic_add,
                        "Templates",
                        "Create mail templates that you use regularly.")
                        .background(R.color.three)
                        .parallax(true)
                )
                .page(new BasicPage(R.drawable.ic_attach,
                        "Attach CV",
                        "Never have to remember where you stored your CV.")
                        .background(R.color.two)
                        .parallax(true)


                ).page(new BasicPage(R.drawable.ic_send,
                        "Send",
                        "Send mails to selected emails.")
                        .parallax(true)
                        .background(R.color.one)
                )

                .swipeToDismiss(true)
                .canSkip(false)
                .bottomLayout(WelcomeConfiguration.BottomLayout.STANDARD_DONE_IMAGE)
                .showPrevButton(true)
                .animateButtons(true)
                .showNextButton(true)
                .exitAnimation(android.R.anim.fade_out)
                .build();
    }
}
