package com.totalchange.discodj.player;

import org.openqa.selenium.WebDriver;

import com.totalchange.discodj.SeleniumUtils;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PlayNextSongStepdefs {
    private WebDriver driver;

    @Before
    public void setup() {
        driver = SeleniumUtils.getInstance().getWebDriver();
    }

    @After
    public void teardown() {
        driver.close();
    }

    @Given("^there are (\\d+) songs and (\\d+) videos in the playlist$")
    public void there_are_songs_and_videos_in_the_playlist(int songs, int videos)
            throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @When("^the current media finishes$")
    public void the_current_media_finishes() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @Then("^the next song in the playlist starts$")
    public void the_next_song_in_the_playlist_starts() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }

    @Then("^the next video in the playlist starts$")
    public void the_next_video_in_the_playlist_starts() throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }
}
