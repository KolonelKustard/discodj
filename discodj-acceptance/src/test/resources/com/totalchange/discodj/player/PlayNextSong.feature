Feature: Play next song on the playlist

  As a groover on the dance floor
  I want the music to carry on
  So I can keep dancing
  
  Scenario: Play next song
    Given there are 10 songs and 0 videos in the playlist
    When the current media finishes
    Then the next song in the playlist starts
  
  Scenario: Play next video
    Given there are 0 songs and 10 videos in the playlist
    When the current media finishes
    Then the next video in the playlist starts
