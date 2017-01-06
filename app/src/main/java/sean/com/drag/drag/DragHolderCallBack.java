package sean.com.drag.drag;

/**
 *
 * 功能描述：这个接口主要被DragAdapter的DragHolder所实现，用于处理相应Item模块的拖拽时、拖拽停止时的相关操作
 *
 **/
public interface DragHolderCallBack {

    void onSelect();

    void onClear();
}
