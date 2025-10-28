# Minesweeper (v1.1.0)

A simple Java Swing implementation of the classic Minesweeper game.  
Version 1.1.0 introduces internal code refactoring for better readability and maintainability.

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/zoranjuras/MineSweeper.git

    Open the project in your preferred IDE (IntelliJ IDEA, Eclipse, or VS Code with Java support).

    Run Minesweeper.java.

Current Features

    9x9, 16x16 or 24x24 board layouts based on selected difficulty

    Randomized mine placement

    Left-click to reveal a tile, right-click to place or remove a flag

    Color-coded numbers for adjacent mines

    Custom tile rendering with centered text

    Game-over and victory messages

    Menu dialog with options for New Game, Settings, and Exit

    Settings dialog to choose between Easy, Medium, and Hard difficulties

    Clean and readable code structure with modular design

Planned Improvements

    Right-click question mark support

    Timer and mine counter

    Visual effects

    Sound effects

Changelog

   Version 1.0.0

    First functional version of the game

    8x8 board with static mine display

    Left-click to reveal, right-click to place flags

    Basic Java Swing user interface


   Version 1.1.0

    Refactored code for better readability and maintainability

    checkMine and countMine now use MineTile objects

    Logic for checking neighbouring tiles centralized in getNeighbourTiles method

    No change in gameplay or user interface

   Version 1.2.0

    Graphical improvements including centered numbers and custom tile rendering

    Board size increased to 16x16 with 40 mines

    Better visual clarity and text rendering

Version 1.3.0

    Introduced Game Menu and Settings dialogs

    Added difficulty selection (Easy, Medium, Hard)

    Dynamic board generation based on difficulty

    Refined visual layout and improved user experience

    Minor performance and stability enhancements

Author

Zoran Juras
Version: 1.3.0
License: MIT

