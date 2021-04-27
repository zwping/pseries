package win.zwping.pseries.base;

import java.io.Serializable;

/**
 * <p>describe：
 * <p>    note：
 * <p> @author：zwp on 2019-04-09 10:27:33 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
public class BaseBean implements Serializable {

    // 与后台约束所有API响应数据格式，其中数据格式1级中必须有code和info，这样方便请求框架底层封装

    /*** 状态码 ***/
    private int code;
    /*** 对应的状态码提示信息 ***/
    private String info;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
