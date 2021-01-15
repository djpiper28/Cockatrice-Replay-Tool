# Cockatrice-Replay-Tool
(Version 1.0) - Release

# Compilation
This tool is built with gradle, use `gradle shadow` to make the executable jar.

# Testing
Use `gradle test` to run the tests. The tests will fail as the test data is not public for data protection reasons (replay files can lead to doxing).
Please provide your own test data.

# Usage
Running the `.jar` in a terminal with no arguments or invalid arguments will show the help screen.
Arguments:
`--replays <replay1> <replay2>...` to analyse the specific files.
`--replay-folder <replayfolder/>` to analyse a folder.

All output is saved as output.csv in the current directory.

# Data specifics
A header is made for each game slot
A player hash of -1 or no value means they are spectators (2^32 -1 of the time)

# Liscening
Public domain for the non-proto buff files
Generated files and protobuff files have liscences available at https://github.com/cockatrice/cockatrice

Made as a tool for the marchesa cEDH tornaments.
