package com.whistle.github;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Callable;

import static com.whistle.github.MainActivity.ROOT;
import static org.junit.Assert.*;
/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    GitHubDataModel model = GitHubDataModel.getInstance();
    Object syncToken = new Object();

    private void lock(){
        synchronized(syncToken) {
            try {
                syncToken.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void unlock(){
        synchronized(syncToken) {
            syncToken.notify();
        }
    }
    @Before
    public void setup() {
        Context appContext = InstrumentationRegistry.getTargetContext();
         model.initialize(appContext,ROOT);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.whistle.github", appContext.getPackageName());
    }

    @Test
    public void IssuesDataModel() {


        model.getIssues(new DataModelListener() {
            @Override
            public void onUpdated() {
                unlock();
            }

            @Override
            public void onError(String error) {
                unlock();
            }
        });

        lock();

        assertFalse( model.size() == 0 );

        model.getComments(model.get(1),new DataModelListener() {
            @Override
            public void onUpdated() {
                unlock();
            }

            @Override
            public void onError(String error) {
                unlock();
            }
        });

        lock();

        assertFalse( model.get(1).comments.size() == 0 );
    }


}
