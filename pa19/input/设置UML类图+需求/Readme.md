## JavaBench中 PA19输入的类(10个类对应10个task)
1. Pipe.java
2. Wall.java
3. TerminationCell.java
4. FillableCell.java
5. Cell.java
6. PipeQueue.java
7. CellStack.java
8. DelayBar.java
9.  Map.java
10. Game.java
## PA19的面向agent的UML类图设计说明
1. Pipe.java（最基础）
2. Wall.java（最简单）
3. TerminationCell.java
4. FillableCell.java
5. Cell.java（依赖上面三个）
6. MapElement
7. PipeQueue.java
8. CellStack.java
9. DelayBar.java
10. Map.java（核心，较复杂）
11. Game.java（最高层，整合所有
12. Coordinate —— 坐标
13. Direction —— 方向枚举（UP, DOWN, LEFT, RIGHT）
14. TerminalCellCreateInfo 
15. TerminalCellType (14和15都是TerminalCell内部类)