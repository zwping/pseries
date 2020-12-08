## 维护一份AOP思路的ktx文件

> AOP: 面向切面编程, 在不改变原有代码同时改变代码执行结果
> 面向学不赢的场景时, 无数大佬的特色框架推出, 这类框架当时使用确实方便, 但其特色各异架构远景不长, 算是饮鸩止渴
> 面向多端开发, 过多特色的框架导致review成本庞大
> 遵循Google原生开发, 遵循原生特色; code成本大, 但二次迭代/review成本降低
> kotlin扩展函数/参数给予了安卓非反射实现AOP功能


### ktx

```kotlin
```

### ToolbarKtx

> Demo >>> ../ToolbarAc.java

```kotlin
T.setTitleOfCenter(title) // 居中的标题 (addTextView形式)
T.setTitleCenter(title)   // 标题居中 (childView遍历)
T.addMenu(itemId...)   // 代码中增加menu, 不再需要创建menu.layout
T.addMenuBadge(itemId...)   // 增加带角标的menu (actionProvider自定义menuView)
T.setStatusBarImmersion() // Toolbar动态marginTop
object ToolbarKtx(){....} // 兼容Java
```

### DiffUtilKtx<B>

> Demo >>> DiffUtil.test

```kotlin
class DiffUtilKtx<B>(...) {
    var oldData : List<B>? // 老数据/当前数据
    fun calculateDiff(data, detectMoves) // 类对象管理oldData
    fun setOnDataDiffListener(lis) // 监听数据是否产生差异 (DiffUtil差量算法)
}
```
