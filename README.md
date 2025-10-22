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

    8x8 board layout

    Random mine placement

    Left-click to reveal a tile, right-click to place or remove a flag

    Display of numbers showing adjacent mines

    Win and game-over messages

    Refactored logic for checking neighbouring tiles

    Improved method structure and code clarity

Planned Improvements

    Right-click question mark support

    Different colours for numbers

    Menu bar

    Timer and mine counter

    Dialog for restarting game or change settings

    Multiple difficulty levels

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

Author

Zoran Juras
Version: 1.1.0
License: MIT

