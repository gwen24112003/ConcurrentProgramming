# ConcurrentProgramming

## Field Test
This program tests the functionality of the `Field` class, which represents an animal field in a farm.  
Each field can store a specific type of animal and has a **maximum capacity of 10 animals**.  

The `TestField` program performs several operations to verify that:  
✔ Animals can be **added** to the field (unless it's full).  
✔ Animals can be **removed** from the field (unless it's empty).  
✔ The **field capacity limit** is enforced correctly.  
✔ The **concurrent access** is safely managed using locks (`ReentrantLock`).  

---

## Enclosure.java and Farm.java in progress
