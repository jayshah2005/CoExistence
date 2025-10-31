# CoExistence

A simple networked multiplayer card game written in Java using sockets, threads, and Swing.  
Originally developed as part of a university project (COSC 2P13), now maintained as a personal project.

---

## Overview

CoExistence is a two-player card game loosely based on Rock–Paper–Scissors.  
Players take turns attacking with cards of four types: Axe, Hammer, Sword, and Arrow.

- Axe defeats Hammer (+1 point)
- Hammer defeats Sword (+1 point)
- Sword defeats Axe (+1 point)
- Arrow defeats any unit (0 points, and can be defeated by any)

The game ends when a player reaches 9 points or when round 5 begins.

---

## Features

- Multithreaded server that pairs players automatically
- Text-based communication protocol (human-readable)
- Swing-based GUI client
- Turn management, move validation, and score tracking
- Compatible with terminal-based clients (e.g., netcat)

---