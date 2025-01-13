# DDD
ddd的一些思考
## 基础概念
1. 实体类 （领域层）
   1. 业务的操作核心对象以及数据
   2. entity最重要的设计原则是保证实体的不变性（Invariants），也就是说要确保无论外部怎么操作，一个实体内部的属性都不能出现相互冲突，状态不一致的情况
   3. 贫血模式在DDD的设计思想下不适用，因为贫血模型往往可以通过setter 来注入数据，这样会导致数据没有注入完全的情况，（同时setter 还会导致数据不一致的情况）
   4. 推荐使用 全参构造方法、工厂模式、builder 模式这三种方式都可以对注入的数据进行检查是否符合基本要求
2. 领域服务（Domain Service）
3. 防腐层
4. 通用层
## 代码编写的知道
1. 一个domain领域的动作需要在 entity 内部完成，如手机号的检查，应该通过手机号相关的entity 进行检查，而不是应该是通过service 或者util的形式。     
为什么： 应该可以让我们的entity可以 执行的行为更加的内聚      
2. 在上文中曾经有提起过，到底应该是Player.attack(Monster)还是Monster.receiveDamage(Weapon, Player)？在DDD里，因为这个行为可能会影响到Player、Monster和Weapon，所以属于跨实体的业务逻辑。
   在这种情况下需要通过一个第三方的领域服务    
   （Domain Service）来完成。

上面一段话的意思解释：     
Player 是一个实体 具有attack的行为        
Monster 一个实体            
Weapon 是一个实体        
但是 player 对 monster 进行attack 并不是的单个entity就能完成的动作，所以需要一个外部的service 来进行组合完成。  
这个对我们的写代码的思考是：


##

