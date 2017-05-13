# AchingMarket

## 一、概述：
    本应用为实现用户登陆/注册/浏览/购买/查看订单的简易项目。

##二、框架：
    数据库：一个数据库，一张账号表，每个账号对应一个订单表；
    通用框架：异步任务：RxJava，Json解析：Gson，代码美化：ButterKnife、Lambda；
    网络请求：OkHttp、Retrofit；
    图像处理：Glide、CircleImageView。

## 三、流程：
    1、应用启动：
    （1）BaseApplication在onCreate中在外部存储根目录创建名为“ArchingMarket”的目录，并在其中建一个库名为“aching.db”的数据库，在该库中建一个名为“accounts”的表，含有account、password、login_state、name、address等字段，分别储存对应用户账号的账号名、密码、登陆状态、昵称、地址等；
    （2）闪屏页动画结束后跳转进登陆/注册页面。
    1．登陆/注册：
     （1）用户输入合法的账号密码后即可正常登陆/注册，当检测到用户输入注册的账号已在表“accounts”中存在时提示账号已存在，或输入登陆的账号在表“accounts”中不存在则提示账号不存在；
    （2）当用户成功注册时，向“account”表里插入一条account、password字段分别对应用户输入的账号信息，并将新建一张表，名为“order_+账号”；当用户登陆成功时数据库会找到“accounts”表里对应的账号，将改账号的“login_state”改为1，并将其它所有的账号“login_state”改为0；
    
        database.insert(TABLE_ACCOUNTS, null, getContentValues(accountInfo));
        database.execSQL(String.format(Locale.getDefault(), SQL_CREATE_ORDER_TABLE, accountInfo.account));
    
    （3）跳转进主界面“MainActivity”。
    2．图书列表：
    （1）数据：采用OkHttp+Retrofit+RxJava框架请求通过豆瓣开放api“https://api.douban.com/v2”请求图书数据。其中OkHttp是针对java&android项目提供的网络请求库；Retrofit是基于OkHttp封装，采用链式调用使网络请求相关代码风格统一，方便管理；RxJava是一个方便操作异步任务的第三方库，可以在子线程和主线程中自由进行切换。所有数据缓存文件存放在“ArchingMarket”目录下的“responses”，示例如下：
    
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    
    （2）界面：图书列表采用RecyclerView展示，并设置对应的BookListAdapter对每一个相同item进行复用。在数据变动时及时通知给Adapter达到数据与界面同步的效果。在用户体验上，采用SwipeRefreshLayout进行下拉刷新，并在RecyclerView监听addOnScrollListener达到用户上滑列表加载更多的效果。每一个item项的图片是使用glide图片处理框架加载而成的，示例如下：
    
        Glide.with(mContext)
                    .load(bookInfo.getImages().getLarge())
                    .into(bookHolder.iv_book_img);
    
    （3）用户在点击RecyclerView任意一个item都会跳转进与之对应的图书详情页。
    3．图书详情：
    （1）图书部分：展示方式为图书封面和，封面背景。背景采用这样的规则：当用户手机android版本在5.0以下时，设置70%的透明度，当高于或等于5.0时，采用图像毛玻璃算法Blur。当用户向下滑动界面时，background和foreground的图书图像会随着滑动程度透明度减小，同是，作为父控件的AppBarLayout透明度增加直至完全不透明，而作为整个页面的ActionBar处于页面顶端，并展示相应标题；
    （2）评论部分：拉取图书信息中的列表信息，采用RecyclerView和对应BookDetailAdapter展示所含评论；
    （3）添加进购物车：点击ActionBar与RecyclerView中间的FloatActionButton时，将新建一条图书信息插入“order+账号”的表中。
    
        ContentValues contentValues = new ContentValues();
            Gson gson = new Gson();
            String bookInfoJson = gson.toJson(orderInfo.bookInfo);
            contentValues.put(FIELD_ORDER_BOOK_ID, orderInfo.bookInfo.getId());
            contentValues.put(FIELD_ORDER_STATE, orderInfo.orderState);
            contentValues.put(FIELD_ORDER_CHECK, orderInfo.bookInfo.isChecked() ? 1 : 0);
            contentValues.put(FIELD_ORDER_QUANTITY, orderInfo.bookInfo.getQuantity());
            contentValues.put(FIELD_ORDER_ADDRESS, orderInfo.bookInfo.getAddress());
            contentValues.put(FIELD_ORDER_BOOK_INFO, bookInfoJson);
            database.insert(TABLE_ORDER + orderInfo.account, null, contentValues);
    
    4．购物车列表：
    （1）读表：读取数据库中名为“order+账号”的表内所用状态为未购买的图书信息，展示在RecyclerView中，展示处理复用图书列表流程；
    （2）编辑：用户点击右上角编辑图标，可对购物车内图书进行左右滑动删除，点击左边CheckBox进行勾选操作，默认为勾选；
    （3）用户购买：点击右下角FloatActionButton，则将表“order+账号”内所有标记为勾选的图书状态为已购买，刷新界面。
    
    /**
     * 提交订单表
     *
     * @param bookIDs
     */
    public synchronized void submitOrder(List<String> bookIDs)
    {
        SQLiteDatabase database = DatabaseHelper.getInstance().getWritableDatabase();
        for (String bookID : bookIDs)
        {
            database.execSQL("UPDATE " + TABLE_ORDER + Global.getAccountInfo().account
                    + " SET " + FIELD_ORDER_STATE + "=1, "
                    + FIELD_ORDER_TIME + "=" + System.currentTimeMillis()
                    + " WHERE " + FIELD_ORDER_BOOK_ID + "=" + bookID
                    + " AND " + FIELD_ORDER_STATE + "=0");
        }
    }
    
    5．设置页：
        展示头像，昵称等个人信息，点击昵称和地址可以编辑，并更新数据库内账号信息。当用户为管理员时，多展示一个“管理按钮”。其次是订单、关于、注销等功能按钮。
    6．订单页：
    	展示读取数据库中“order+账号”表内所用状态为已购买的图书信息。
    7．管理页：
        默认隐藏，当登陆账号是管理员账号时显示，读取数据库中“accounts”除了自己以外的账户信息并展示。
