# ReadiKids 📚 — Android Reading App

A gamified reading education app for children aged 3–8, built with Jetpack Compose.

---

## 🚀 Quick Start: Build & Deploy to Play Store

### Step 1 — Prerequisites
- **Android Studio Hedgehog** (2023.1.1) or newer → https://developer.android.com/studio
- **JDK 17** (bundled with Android Studio)
- **Google Play Developer Account** → https://play.google.com/console ($25 one-time fee)

---

### Step 2 — Open the Project
1. Open **Android Studio**
2. Click **File → Open**
3. Select the `ReadiKids/` folder
4. Wait for Gradle sync to complete (first time takes 5–10 min)

---

### Step 3 — Add Fonts (Required)
The app uses **Nunito** font. Download and place in `app/src/main/res/font/`:

1. Go to https://fonts.google.com/specimen/Nunito
2. Download the font family
3. Rename and place these 4 files:
   - `nunito_regular.ttf`
   - `nunito_bold.ttf`
   - `nunito_extrabold.ttf`
   - `nunito_black.ttf`

Into: `app/src/main/res/font/`

---

### Step 4 — Firebase Setup (Optional for cloud sync)
1. Go to https://console.firebase.google.com
2. Create project "ReadiKids"
3. Add Android app with package `com.readikids.app`
4. Download `google-services.json`
5. Place it in `app/` folder (next to `build.gradle`)

> **Note:** If you skip Firebase, remove these lines from `app/build.gradle`:
> - `id 'com.google.gms.google-services'` (plugins block)
> - Firebase dependencies (3 lines at bottom)
> - And from root `build.gradle`: `id 'com.google.gms.google-services' version '4.4.0' apply false`

---

### Step 5 — Add Launcher Icons
Replace placeholder icons with real ones:
1. In Android Studio: **Right-click `res/` → New → Image Asset**
2. Choose **Launcher Icons (Adaptive and Legacy)**
3. Select a book/reading emoji or custom artwork
4. Click **Next → Finish**

---

### Step 6 — Build Release APK / AAB
#### For Play Store (use AAB):
```bash
# In Android Studio terminal:
./gradlew bundleRelease
```
Output: `app/build/outputs/bundle/release/app-release.aab`

#### Or via Android Studio:
1. **Build → Generate Signed Bundle / APK**
2. Choose **Android App Bundle**
3. Create a new keystore (SAVE THIS FILE — you need it forever!)
   - Key store path: save somewhere safe (e.g. `~/readikids-keystore.jks`)
   - Key alias: `readikids`
   - Password: (create a strong password — DO NOT LOSE IT)
4. Click **Next → Release → Create**

---

### Step 7 — Publish to Google Play Store
1. Go to https://play.google.com/console
2. **Create app** → Fill in:
   - App name: **ReadiKids - Learn to Read**
   - Default language: English
   - App or game: **Game**
   - Free or paid: **Free**
3. **Production → Releases → Create new release**
4. Upload your `.aab` file
5. Fill in **Store listing**:
   - **Short description:** Fun reading games for kids aged 3-8. Learn letters, words & stories!
   - **Full description:** (see below)
   - Screenshots: At least 2 phone screenshots
   - Feature graphic: 1024×500 banner
6. Fill in **Content rating** → Complete questionnaire (select: children's app, no violence)
7. Set **Target audience** → Under 13 (triggers COPPA compliance)
8. **Pricing & distribution** → Free, select countries
9. Submit for review (takes 1–7 days for first submission)

---

## 📱 Play Store Description Template

**Short (80 chars):**
```
ReadiKids: Fun reading games for kids 3-8. Learn letters & words!
```

**Full description:**
```
📚 ReadiKids — The Fun Way to Learn Reading!

ReadiKids helps children aged 3-8 build essential reading skills through exciting, 
colorful games. Your child will be so engaged they won't even realize they're learning!

🎮 6 EXCITING READING GAMES:
• 🔤 ABC Adventure — Tap letters and hear their sounds
• 🃏 Word Match — Match words to pictures
• 🎵 Rhyme Time — Find all the rhyming words
• 📖 Story Builder — Complete the story with the right word
• ✏️ Spell Blast — Unscramble letters to spell words
• 👁️ Sight Word Ninja — Tap the target word before time runs out!

⭐ KEEPS KIDS ENGAGED:
• Earn XP stars for every correct answer
• Level up through 5 reading levels
• Daily streaks reward consistent practice
• Unlock achievement badges
• Bouncy animations and instant feedback

📊 5 READING SKILL LEVELS:
• Tiny Stars (3-4 years): Letters and sounds
• Moon Riders (4-5 years): CVC words and rhymes
• Sun Chasers (5-6 years): Blending and spelling
• Star Gazers (6-7 years): Sight words and sentences
• Sky Heroes (7-8 years): Reading comprehension

👨‍👩‍👧 PARENT FRIENDLY:
• No ads — ever
• No in-app purchases
• No social features
• Progress tracking for each skill
• Works offline

Start your child's reading journey today! 🚀
```

---

## 🏗️ Project Structure

```
ReadiKids/
├── app/src/main/java/com/readikids/app/
│   ├── MainActivity.kt              # App entry point
│   ├── ReadiKidsApp.kt             # Application class (Hilt)
│   ├── data/
│   │   ├── model/Models.kt         # Data classes, enums, game definitions
│   │   ├── db/AppDatabase.kt       # Room database + DAO
│   │   └── repository/             # DataStore preferences
│   ├── di/AppModule.kt             # Hilt dependency injection
│   ├── navigation/NavGraph.kt      # Navigation routes
│   ├── ui/
│   │   ├── theme/                  # Colors, typography, theme
│   │   └── screens/
│   │       ├── Components.kt       # Reusable UI components
│   │       ├── OnboardingScreen.kt # First-launch profile setup
│   │       ├── HomeScreen.kt       # Dashboard with all games
│   │       ├── ProgressScreen.kt   # Stats and achievements
│   │       └── games/
│   │           ├── AbcAdventureScreen.kt
│   │           ├── WordMatchScreen.kt
│   │           ├── RhymeTimeScreen.kt
│   │           ├── StoryBuilderScreen.kt
│   │           ├── SpellBlastScreen.kt
│   │           └── SightWordNinjaScreen.kt
│   └── viewmodel/MainViewModel.kt  # State management
└── app/src/main/res/
    ├── values/ (strings, colors, themes)
    ├── drawable/ (icons)
    └── xml/ (backup rules)
```

---

## 🛠️ Tech Stack
| Technology | Purpose |
|---|---|
| Kotlin | Primary language |
| Jetpack Compose | Declarative UI |
| Hilt | Dependency injection |
| Room Database | Local data persistence |
| DataStore | User preferences |
| Navigation Compose | Screen navigation |
| Android TTS | Text-to-speech (ABC game) |
| Firebase Auth | Optional user accounts |
| Firebase Firestore | Optional cloud sync |
| SplashScreen API | Android 12+ splash screen |

---

## 🎨 Customization

### Adding New Words to Games
- **Spell Blast:** Edit `SPELL_WORDS` list in `SpellBlastScreen.kt`
- **Rhyme Time:** Edit `RHYME_ROUNDS` list in `RhymeTimeScreen.kt`
- **Story Builder:** Edit `STORY_QUESTIONS` list in `StoryBuilderScreen.kt`
- **Sight Words:** Edit `SIGHT_WORD_SETS` list in `SightWordNinjaScreen.kt`
- **Word Match:** Edit `ALL_PAIRS` list in `WordMatchScreen.kt`

### Changing Colors
Edit `app/src/main/java/com/readikids/app/ui/theme/Color.kt`

### Changing App Package Name
1. Rename folder: `com/readikids/app` → `com/yourname/yourapp`
2. Update `applicationId` in `app/build.gradle`
3. Update `namespace` in `app/build.gradle`
4. Update `package` in `AndroidManifest.xml`

---

## ⚠️ Important Notes

1. **Keystore backup:** The release keystore file is critical. If lost, you can never update your app on Play Store. Back it up in multiple secure locations.

2. **Version codes:** Every Play Store update needs a higher `versionCode` in `app/build.gradle`.

3. **COPPA compliance:** Since this targets under-13, Google enforces additional restrictions. The app has no ads and no data collection features that would conflict with COPPA.

4. **Font license:** Nunito is available under SIL Open Font License 1.1, free for commercial use.

---

## 📞 Support
For issues, email: support@readikids.app (update with your email)
