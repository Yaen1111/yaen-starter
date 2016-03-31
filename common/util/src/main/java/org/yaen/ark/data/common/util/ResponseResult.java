package org.yaen.ark.data.common.util;

/**
 * 统一返回对象
 * 
 * @author baodk
 */
public class ResponseResult<T> implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 返回对象类型数据 */
	private T content;

	/** 信息编码 */
	private String code = "200000";

	/** 提升信息 */
	private String message = "success";

	 
	public String toJson() {
		return FastJSONHelper.serializeWithNull(this);
	}

	public String toJson(String[] includesProperties) {
		return FastJSONHelper.serialize(this, includesProperties);
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ResponseResult(T content) {
		super();
		this.content = content;
	}

	public ResponseResult() {

	}

	
	
	
}
