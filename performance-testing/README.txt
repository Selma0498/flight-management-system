- AU stands for Active Users (tests done on 1, 10, 100 and 1000 active users)
- ramps up to 1110 refers to following injection in gatling simulations: 
  atOnceUsers(10), constantUsersPerSec(10) during (10 seconds), rampUsers(1000) during (60 seconds)
- Test Case I: User login and flight browsing
- Test Case II: Admin login and flight creation
- Test Case III: Admin login, flight creation and deletion/cancellation
- Test Case IV: User login and flight booking