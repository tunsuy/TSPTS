package com.sangfor.moa.cch.message;

import java.io.File;
import java.io.FileReader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/**
 * 用户解析json数据
 * @author cch
 *
 */
public class JsonDataUtil {

	private JsonObject json_object = null;
	
	/**
	 * 参数检查
	 * @throws Exception
	 */
	private void checkInput() throws Exception{
		String srv_name = getSrvName();
		if(srv_name.isEmpty()){
			throw new Exception("json file need input srv_name!");
		}
		
		String srv_code = getSrvCode();
		if(srv_code.isEmpty()){
			throw new Exception("json file need input srv_code!");
		}
		
		String inf_name = getInfName();
		if(inf_name.isEmpty()){
			throw new Exception("json file need input inf_name!");
		}
		
		String inf_code = getInfCode();
		if(inf_code.isEmpty()){
			throw new Exception("json file need input inf_code!");
		}
		
		String inf_rsp = getInfRsp();
		if(inf_rsp.isEmpty()){
			throw new Exception("json file need input inf_rsp!");
		}
		
		JsonObject parm = getParm();
		if(null == parm || parm.isJsonNull()){
			throw new Exception("json file need input parm!");
		}
	}
	
	public void init(String path) throws Exception{

		if(null == path){
			throw new Exception("path is null");
		}
		File file =new File(path);
		if  (!file.exists() || file.isDirectory()){
			throw new Exception("file is not exists");
		}

		JsonParser parse =new JsonParser();  //创建json解析器
        try {
        	this.json_object = (JsonObject) parse.parse(new FileReader(path));  //创建jsonObject对象 
        } catch (Exception e) {
        	throw new Exception("open json file failed , please check the json file");
        }
        checkInput();
	}
	
	
	public String getSrvName(){
		return this.json_object.get("srv_name").getAsString();
	}
	
	public String getSrvCode(){
		return this.json_object.get("srv_code").getAsString();
	}
	
	public String getInfCode(){
		return this.json_object.get("inf_code").getAsString();
	}
	
	public String getInfName(){
		return this.json_object.get("inf_name").getAsString();
	}
	
	public String getInfRsp(){
		return this.json_object.get("inf_rsp").getAsString();
	}
	
	public JsonObject getParm(){
		return this.json_object.get("parm").getAsJsonObject();
	}
	
	public String toFormatString(){
		return JsonDataUtil.formatJson(this.json_object.toString());
	}
	
	/**
	 * 格式化
	 * @param jsonStr
	 * @return
	 * @author   cch
	 * @Date   
	 */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 添加space
     * @param sb
     * @param indent
     * @author   cch
     * @Date  
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append("      ");
        }
    }

}
