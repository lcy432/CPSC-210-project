# Gacha Statistics Tracker

## Project Description

**What will the application do?**  
- It records each gacha pull, including the specific pull count at which a 5-star or 4-star item appears.  
- It calculates the expected value (probability) of getting both 5-star and 4-star items over time.  
- It allows users to compare their own pull rates with the rates officially published by the game company.  

**Who will use it?**  
- *Players* of gacha-based games who want to verify whether their pull outcomes match the game's stated probabilities.  
- *Consumers* seeking to make informed decisions and control their spending on in-game purchases.  

**Why is this project of interest?**  
- **Personal Relevance:** I frequently play games with gacha mechanics, and this tool will provide convenience in analyzing my own pulls. 

- **Growing Market:** Gacha mechanics are increasingly prevalent in modern games. Offering a data-driven tool helps a wide audience of players who need accurate and transparent pull statistics.

## User Stories

1. **As a user, I want to be able to add a new gacha pull record**  
   - Include the 5-star item (and whether it’s the desired item) and the number of 4-star items from that pull.

2. **As a user, I want to be able to view all recorded gacha pulls**  
   - See a list of past pull records, including details on 5-star and 4-star items.

3. **As a user, I want to be able to remove or correct a gacha pull record**  
   - Fix mistakes or delete any record that was entered incorrectly.

4. **As a user, I want to be able to calculate the expected value for 5-star and 4-star items**  
   - Determine how often 5-star (desired or undesired) and 4-star items appear in total.

5. **As a user, I want to be able to set a threshold to compare my 5-star desired item rate**  
   - Quickly check if my actual rate is above or below an official or personal benchmark.

6. **As a user, I want to be able to save the entire state of my gacha pull history to a file**  
   - Ensure that all my recorded pulls, along with their details, are preserved so that I can back up my data or transfer it to another device.

7. **As a user, I want to be able to load a previously saved state from a file and resume exactly where I left off**  
   - Restore all my gacha pull records and related settings from a saved file, allowing me to continue tracking my pull statistics without data loss.

# Instructions for End User

- You can generate the first required action related to the user story "adding multiple pulls to history" by clicking "Add New Pull".
- You can generate the second required action related to the user story "view all recorded gacha pulls" directly through the panel.
- You can generate the third required action related to the user story "remove or correct a gacha pull record" by clicking "Delete Pull".
- You can locate my visual component by clicking "Stats" and comparing probabilities.
- You can save the state of my application by clicking "Save Data".
- You can reload the state of my application by clicking "Load Data".

## Phase 4: Task 2

- Example event log output from running the program:

- Sun Mar 30 05:06:05 PDT 2025
- Added pull record: Pull #1, Desired 5-star: true, 4-star count: 3, Total draws: 34
- Sun Mar 30 05:06:09 PDT 2025
- Removed pull record: Pull #1

## Phase 4: Task 3

While creating the UML diagram for this project, I realized that the current design clearly separates responsibilities among the model, persistence, and ui packages, making the codebase structured and maintainable. However, if given additional time, I would consider refactoring the project further by extracting some of the complex interaction logic from the GachaTrackerGUI class into dedicated Controller classes. This approach aligns better with the MVC (Model-View-Controller) pattern and would help ensure that the GUI remains solely responsible for the presentation layer, thereby increasing maintainability and reducing complexity.

Additionally, the rendering logic in the StatsBarChartPanel class could be moved into a specialized visualization component or factory. This change would simplify the addition of new visualization types or modifications to existing ones in the future, without altering core UI components.