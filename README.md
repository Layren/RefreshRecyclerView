# RefreshRecyclerView
带刷新功能的recycleView

     注:此module为个人项目经历整理,具有以下几个特性
        1.非标准化
              写法习惯语法命名之类的,仅仅为个人习惯而已,并非遵守通用代码规则;
              注释说明也仅仅是简单标注
        2.非结构最优
              方法,监听,回调..之类一些多余的,不完全的...不是最好的.只是没有太多的精力去调整;
        3................


    adapter:

        RefreshViewAdapter:
          继承于BaseQuickAdapter
          构造方法参数:
            layoutResId:Adapter布局Id
            listener:RefreshViewAdapterListener,Adapter 布局回调
          重写convert方法,调用listener 的 setHolder();

        RefreshViewMultiItemAdapter:
          继承于BaseMultiItemQuickAdapter
          构造方法参数:
            listener:RefreshViewMultiItemAdapterListener:Adapter 布局回调
            layoutIds:Adapter布局数组,按顺序添加,数组下标对应MultiModel.getItemType() 从0开始;
          setViewType(layoutIds); 添加多布局到Adapter
          重写convert方法,调用RefreshViewAdapterListener 的 setHolder()


    interfaces:

        OnRefreshListener:
          RefreshRecyclerView监听;
          onRefresh():刷新事件;
          onLoadMore():加载更多事件;

        RefreshViewAdapterListener<T>
:         Adapter单布局回调,支持泛型;
          setHolder(holder,item);布局填充回调;

        RefreshViewMultiItemAdapterListener<T extends MultiModel>:
          Adapter多布局回调,支持泛型,泛型继承于MultiModel
          setHolder(holder, item,  itemType); 布局填充回调
            itemType:数据类型,MultiModel.getItemType(),对应布局为 adapter中 layoutIds 数组中的下标;
    model:

        MultiModel:
          继承于AbstractExpandableItem;用于多布局折叠效果;
          实现MultiItemEntity接口;用于多布局效果

          itemType:数据对应布局标识
          spanSize:子View网格布局占用宽度
          level:数据层级,用于折叠效果(默认1,逐级递增)

    view:

        FullyGridLayoutManager:
          布局管理器
          继承于GridLayoutManager:

          adapter:单布局adapter
          multiAdapter:多布局adapter

          构造方法参数:
            context:
            spanCount:网格布局列数
            adapter:单布局adapter
             multiAdapter:多布局adapter

          重写onMeasure()方法,校正子View宽高;

        ItemDecoration:
          自定义分割线
          继承于RecyclerView.ItemDecoration

          构造方法参数:
          height:分割线高度(宽度)
          orientation:分割线于View的位置(Gravity.TOP||Gravity.BOTTOM||Gravity.LEFT||Gravity.RIGHT)
          color:颜色(@ColorInt) (暂时仅支持纯色)
          isFrame:是否添加边框(用于多列布局)

          (此类已知Bug):
            在多布局且布局spanSize 不相同时会出现 分割线错乱问题;

        NestedRecyclerView<T>:
          嵌套RecyclerView,用于RecyclerView和ScrollView嵌套(不建议,数据会全部加载并占用大量内存,建议使用多布局)
          继承于RecyclerView;

          context;
          count://布局列数,默认1,可配置xml属性 app:counts="1"

        RefreshRecyclerView<T>:
          带刷新RecyclerView,自定义布局,继承于LinearLayout

          count://布局列数,默认1,可配置xml属性 app:counts="1"
          hideRefresh://隐藏刷新  可配置xml属性 app:hideRefresh="true"










# RefreshRecyclerView
