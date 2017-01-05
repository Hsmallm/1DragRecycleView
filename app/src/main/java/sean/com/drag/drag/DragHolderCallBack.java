package sean.com.drag.drag;

/**
 * Author: zhuwt
 * Date: 2016/7/4 16:35
 * Description: 说明
 * PackageName: sean.com.drag.drag.DragHolderCallBack
 * Copyright: 杭州存网络科技有限公司
 *
 * 功能描述：这个接口主要被DragAdapter的DragHolder所实现，用于处理相应Item模块的拖拽时、拖拽停止时的相关操作
 *
 **/
public interface DragHolderCallBack {

    void onSelect();

    void onClear();
}
