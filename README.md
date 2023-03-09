
# Duex - Homework reminder app

This is a mobile application that allows users to create reminders and get notified about them. The application has several features including login, registration, creating, editing, and deleting reminders, as well as scheduling and triggering notifications. It also allows users to add location triggers for reminders.


## Badges

Add badges from somewhere like: [shields.io](https://shields.io/)

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)
[![AGPL License](https://img.shields.io/badge/license-AGPL-blue.svg)](http://www.gnu.org/licenses/agpl-3.0)

## Features

- Create an account 
- Make a homework
- Chanage username and profile picture 
- Alternative login methods(PIN, Pattern, Fingerprint)
- Logout
- Firebase
- Add location
- Create new homework
- Edite and Delete homework
- Voice to speech


## Installation

To install this app on your mobile phone, follow these steps:

- Clone this repository to your local machine
- Open the project in Android Studio
- Connect your mobile phone to your computer and enable USB debugging
- Click on the "Run" button in Android Studio to install the app on your phone

Contributions are welcome! Please submit a pull request or open an issue if you find any bugs or have suggestions for new features.

## Usage

<em><strong>Login & Activities</strong></em>

The app includes user interfaces for the login screen, main (message) screen, and profile view. The login screen can be of the user's choosing, and the main activity contains a list element populated with reminder messages. The main activity also includes interface elements that allow users to create a new reminder and log out.

Username and password checks are implemented using predetermined combinations stored and read from Firebase authentication & Firebase realtime database. The activity changes to the main screen on correct login information, and navigation between interfaces is enabled.

<em><strong>Managing a List of Reminders</strong></em>

The app enables users to manage the list of reminders by adding, editing, and removing them. The 'add reminder' button can use an interface of the separate activity. Users can edit a reminder, and they can remove a reminder using prompts or a separate button to show the 'remove reminder' option on each.

Reminders are stored in a separate class with attributes such as message, location_x, location_y, reminder_time, creation_time, creator_id, and reminder_seen. The app stores reminders in a Firebase realtime database.

<em><strong>Scheduling & Triggering Notifications</strong></em>

Users can schedule a reminder given a time and be informed of new reminders via a notification. The app uses WorkManager to check for new reminders and calculates the time until the reminder occurs. When the job is due, the app shows a notification with the reminder information or redirects the user to the app.

<!-- Reminders with a location or timing requirement are hidden from the list, and a "show all" or similar feature can be used to ensure that all reminders can be edited or removed. -->

<em><strong>Location-Triggered Reminders</strong></em>

Users can add a location trigger for a reminder and set a reminder area with a specified distance within that location. The app shows and sends a notification to the user when they are within the reminder's area and/or at the correct time. Users can also select a location from the map to show reminders that are near or at that location.

## Screenshots

<p align="center"><img width="30%" src="https://user-images.githubusercontent.com/6293254/217293701-d18df68d-c7c7-48cb-a6cd-7c0656e59e16.png"></p>
<p align="center"><img width="30%" src="https://user-images.githubusercontent.com/6293254/219363279-a7c0372c-cada-4632-8517-f326efcd8389.png"></p>
<p align="center"><img width="30%" src="https://user-images.githubusercontent.com/6293254/219363507-120820f8-e2c8-4e0a-90c3-58da7f5629ec.png"></p>



## Acknowledgements

This app was created as part of a homework assignment for a mobile development course. The requirements and guidelines for the assignment were provided by the course instructor.
