# FLTR: Foreign Language Text Reader

> **⚠️ IMPORTANT NOTICE: This is an UNOFFICIAL fork**
> 
> This repository was created in 2017 when the FLTR project appeared to be abandoned. However, the original developers have since resumed maintenance of FLTR.
> 
> **For the official, up-to-date version of FLTR, please visit:**
> - **Official Website:** https://foreign-language-text-reader.sourceforge.io
> - **Official SourceForge Project:** https://sourceforge.net/projects/foreign-language-text-reader/
> 
> This fork is kept as a historical archive and is NOT actively maintained. Users are strongly encouraged to use the official version instead.

## What Problem Does This Solve?
FLTR (Foreign Language Text Reader) is a specialized tool designed to help language learners read texts in foreign languages. It addresses the challenge of reading comprehension by providing an interactive environment where users can look up unknown words, save vocabulary, and track their learning progress.

## Who Is This For?
- Language learners at intermediate to advanced levels
- Language teachers creating reading materials for students
- Self-study language enthusiasts
- Anyone who wants to improve reading comprehension in a foreign language

## Current Implementation Status
- ✅ Text import and management
- ✅ Word lookup and vocabulary tracking
- ✅ Term status tracking (known/unknown/learning)
- ✅ Language definition management
- ✅ Fuzzy search capabilities
- ✅ User preferences and settings
- 🚧 Modern UI improvements
- 🚧 Additional language support
- 📋 Mobile/tablet support
- 📋 Cloud synchronization

## Setup Instructions

### Prerequisites
- Java Runtime Environment (JRE) 8 or higher
- Basic understanding of Java applications

### Installation
1. Clone the repository:
   ```
   git clone https://github.com/nczempin/fltr.git
   cd fltr
   ```

2. Compile the source code:
   ```
   javac -d bin -sourcepath src -cp lib/miglayout-4.0.jar src/fltrpackage/Application.java
   ```

3. Copy resources to the bin directory:
   ```
   cp src/fltrpackage/icon128.png bin/fltrpackage/
   ```

4. Create a runnable JAR file (optional):
   ```
   jar cvfm fltr.jar manifest.txt -C bin .
   ```

### Running the Application
1. Run from compiled classes:
   - On Linux/Mac:
     ```
     java -cp "bin:lib/miglayout-4.0.jar" fltrpackage.Application
     ```
   - On Windows:
     ```
     java -cp "bin;lib/miglayout-4.0.jar" fltrpackage.Application
     ```

2. Or run from the JAR file (if created):
   ```
   java -jar fltr.jar
   ```

## Project Scope

### What This IS
- A tool for reading and studying foreign language texts
- A vocabulary management system for language learners
- A fork that maintains and improves upon the abandoned original project
- A desktop Java application for personal language learning

### What This IS NOT
- Not a translation tool or dictionary (though it helps with vocabulary)
- Not a language course or complete learning system
- Not optimized for mobile devices or tablets
- Not a commercial product with support services

## Repository Structure
- `src/fltrpackage/` - Main application source code
  - `Application.java` - Main entry point
  - `StartFrame.java` - Initial UI frame
  - `TextFrame.java` - Text reading interface
  - `TermFrame.java` - Vocabulary management
  - `Language.java` - Language definition handling
  - `Text.java` - Text content management
  - `Term.java` - Vocabulary term representation
  - Various UI components and utility classes

## Basic Usage
1. Start the application
2. Create or select a language definition
3. Import or create a text in your target language
4. Click on words you don't know to mark them and add translations
5. Track your progress as you learn new vocabulary

## Fork History
This repository was created in July 2017 when the original FLTR project appeared to be abandoned on SourceForge. The code was imported to preserve it and make it available via Git.

The original developers resumed activity in 2020 and released multiple versions (0.8.10, 1.0.0, 1.1.0, 1.2.0, 1.3.0) before releasing FLTR version 1.4.0 in June 2021. They continue to maintain both FLTR and their related project LWT (Learning with Texts).

### Bug Fixes in This Fork
This fork contains two bug fixes from 2020 that have NOT been incorporated into the official version 1.4.0:
1. **NPE fix for missing icons** - Prevents crashes when icon files cannot be loaded
2. **Java version check fix** - Fixes version comparison for Java 9+ (though the official version removed this check entirely)

### Other GitHub Forks
- **magnus-ISU/foreign-language-text-reader** - Another GitHub fork with prebuilt JAR files and Linux installation scripts: https://github.com/magnus-ISU/foreign-language-text-reader

## Development Status
**This fork is NOT actively maintained.** It exists as a historical archive of the 2017 codebase with minor bug fixes. 

For active development and support, please use the official FLTR at:
- https://foreign-language-text-reader.sourceforge.io
