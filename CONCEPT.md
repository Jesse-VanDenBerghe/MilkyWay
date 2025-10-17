# KMP Baby Feeding Timer App Concept

This document outlines the features and functionality for a **Kotlin Multiplatform (KMP)** application designed to help parents track their baby's feeding times.

---

## Core Features

The initial version of the app will focus on three main areas:
- **Timeline**
- **Timer Screen**
- **Post-Feeding Summary**

---

### 1. Timeline Screen

This will be the main screen of the app.

- **Reverse Chronological Order:**  
  Displays a list of all past feeding sessions, with the most recent session at the top.

- **Feeding Moment Items:**  
  Each item in the list represents a single feeding session and will display key information (e.g., time, amount).

- **Time Between Feedings:**  
  Between each feeding session item, the timeline will clearly show the duration that has passed between the end of one feeding and the start of the next.

---

### 2. Timer Screen

This is the active tracking screen for a feeding session.

- **Stopwatch Timer:**  
  A primary timer that functions like a stopwatch to track the duration of the feeding.

- **Mode Switching:**  
  A “Reset” or “Switch” button will allow the user to seamlessly switch from the primary drinking timer to a secondary “Burp Timer” without losing the session’s context.

- **Virtual Bottle:**  
  - A visual representation of a baby bottle that gradually empties as the timer progresses.  
  - The emptying animation will be calculated based on a user-defined minimum feeding time and the total amount of milk to be given.

- **Screen Dimmer / Night Mode:**  
  - An easily accessible button or toggle to lower the screen’s brightness.  
  - This allows for comfortable use in low-light environments, preventing disturbance to the baby.

---

### 3. Post-Feeding Summary Screen

This screen appears after the user stops the “Burp Timer.”

- **Review and Edit:**  
  Users can review the details of the just-completed session.

- **Editable Fields:**
  - **Amount Consumed:** Adjust the final amount the baby actually drank.  
  - **Pace:** A score or note on how quickly the baby drank.  
  - **Satisfaction:** A simple rating system (e.g., stars, emojis) to note how well the feeding went.

- **Save Session:**  
  After confirming the details, the user saves the session, which then gets added to the top of the Timeline Screen.

---

## Future Enhancements (Post-MVP)

The following features are planned for future releases to expand the app’s functionality:

- **Multiple Feeding Types:**  
  Introduce options to track different feeding methods, such as breastfeeding (with timers for left/right) and pumping sessions.

- **Presets:**  
  Allow users to create and save presets before starting a session (e.g., “Morning Feed - 150ml”).

- **Theming:**
  - **App-wide Theming:** Offer different color schemes and themes for the entire application.  
  - **Timer Theming:** Provide specific visual customizations for the timer screen to make it more personal and engaging.
