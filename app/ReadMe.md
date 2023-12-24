# Retro - Let's Go

Retro - Let's Go is an app for retro gaming enthusiasts

# Features

- Save the details of your favourite arcades including their location and an image
- See the details of other users saved arcades
- View both your own and other users arcades on a map showing your current location, allowing you to quickly find arcades around you
- Create a list of your favourite retro video games and the high score you achieved in them
- Dark Mode supported
- User details and arcade details as well as images stored remotely in Firebase.

## API's used

- Googles Firebase console used for RealTime Database support and Cloud Storage of files
- OAuth via the Google Console to allow authentication using Google Credentials
- Google Maps SDK for placing location pin of the arcades

## UML & Class Diagrams

https://github.com/Original-Gib/Retro_Lets_Go/blob/59b47a90bf014e678d39a9129a83672fecfc59a8/app/RetroLetsGoUml.png

## UX Approach

Menu based navigation which each fragment linking to related ones.
Eg for the user to navigate to the arcadeLists they simply need to select the Arcades menu option in the sliding nav drawer, from there though the user can navigate further down to the arcadeFragment if adding a new arcade or to the arcadeDetailsFragment if viewing a current arcade.

## DX Approach

MVVM approach used for architecture of the project. I had ininitially gone about refactoring the app to use the MVP approach, however I ultimately decided on using the MVVM structure. However as I had already made some commits to Github with the MVP changes I encountered some issues relating to this.

## Git Approach

The project has its own repo within my Github account. Version 1 of the project has been tagged within Github with improvments made during assignment 2 coming after the version 1 tag. Final release for assignment 2 has been tagged with a version 2 tag.

I utilised a development branch and a main branch to allow me to work and develop functional code whilst not impacting the stability of the main branch. Once a release was pushed to the remote develop branch I would then pull this into the main branch and push the changes to main.

One issue I encountered was regarding having made changes to the project to utilise MVP architecture, I then went back to a historic commit and pulled that into my develop branch to change the architecture to MVVM instead. However once I pushed the changes to the remote branch I was unable to pull these into the main branch as the main branch was showing as being ahead of the develop branch due to me working off an old commit.

To resolve this I had to revert the main branch back to a previous commit version to remove the changes which had been made to bring it in line with MVP. Once this was done I pushed to the repo, pulled in the develop again and pushed again to the repo. This allowed me to get thorugh this error and continue working.

## Personal Statement

Android is not an environment I am too familiar with, coupled with the fact that mobile app development is a step away from what I would be used to in web development. Saying that I did find the content very interesting and informative. I thought it was delivered in a very clear way that was easy to comprehend. That being said I think it has been the module which I have found most difficult on the course. I'm not currently working in a space related to mobile development, however it is something which I will continue to explore in my own personal projects, as the freshness of the technology relating to mobile development is something which really excites me.

## References

- Search feature within a recycler view - https://www.youtube.com/watch?v=SD097oVVrPE
- Game Camera feature - https://developer.android.com/guide/topics/media/camera
- Night Mode feature - https://www.section.io/engineering-education/how-to-implement-dark-mode-in-android-studio/

