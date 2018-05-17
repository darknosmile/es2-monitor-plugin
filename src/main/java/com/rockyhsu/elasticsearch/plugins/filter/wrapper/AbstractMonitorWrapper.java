package com.rockyhsu.elasticsearch.plugins.filter.wrapper;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.suggest.SuggestRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentHelper;

import java.util.Arrays;

public abstract class AbstractMonitorWrapper implements MonitorWrapper {

    private final ESLogger logger = Loggers.getLogger(AbstractMonitorWrapper.class);

    public String parseRequestDetail(String action , ActionRequest request){
        String res = null;
        StringBuffer sb = new StringBuffer();
        try {
            if(request instanceof SearchRequest){
                String[] indices = ((SearchRequest) request).indices();
                String[] types = ((SearchRequest) request).types();
                if(null!=indices){
                    sb.append("reqIndex=").append(convertArrays(((SearchRequest) request).indices())).append(",");
                }
                if(null!=types){
                    sb.append("reqType=").append(convertArrays(((SearchRequest) request).types())).append(",");
                }
                sb.append("detail=").append(XContentHelper.convertToJson(((SearchRequest) request).source(), false));
                res = sb.toString();
            }else if(request instanceof GetRequest){
                sb.append("reqIndex=");
                sb.append(((GetRequest) request).index());
                sb.append(",reqType=");
                sb.append(((GetRequest) request).type());
                sb.append(",detail=id:");
                sb.append(((GetRequest) request).id());
                res = sb.toString();
            }else if(request instanceof IndexRequest){
                sb.append("reqIndex=");
                sb.append(((IndexRequest) request).index());
                sb.append(",reqType=");
                sb.append(((IndexRequest) request).type());
                sb.append(",detail=");
                sb.append(XContentHelper.convertToJson(((IndexRequest) request).source(), false));
                res = sb.toString();
            }else if(request instanceof DeleteRequest){
                sb.append("reqIndex=");
                sb.append(((DeleteRequest) request).index());
                sb.append(",reqType=");
                sb.append(((DeleteRequest) request).type());
                sb.append(",detail=id:");
                sb.append(((DeleteRequest) request).id());
                res = sb.toString();
            }else if(request instanceof UpdateRequest){
                sb.append("reqIndex=");
                sb.append(((UpdateRequest) request).index());
                sb.append(",reqType=");
                sb.append(((UpdateRequest) request).type());
                sb.append(",detail=id:");
                sb.append(((UpdateRequest) request).id());
                res = sb.toString();
            }else if (request instanceof SuggestRequest){
                String[] indices = ((SuggestRequest) request).indices();
                if(null!=indices){
                    sb.append("reqIndex=").append(convertArrays(((SuggestRequest) request).indices())).append(",");
                }
                sb.append("detail=").append(XContentHelper.convertToJson(((SuggestRequest) request).suggest(), false));
                res = sb.toString();

            }else if(request instanceof BulkRequest){
                for(ActionRequest actionRequest:((BulkRequest) request).requests()){
                    sb.append(parseRequestDetail(null,actionRequest));
                    sb.append(",");
                }
                res = "detail={"+sb.substring(0,sb.length()-1)+"}";
            }
        } catch (Exception e) {
            logger.error("parseRequestDetail action={},exception={}",action,e);
        }
        return res;
    }

    public String convertArrays(String[] arrays){
        String res = null;
        if(null!=arrays){
            if(arrays.length==1){
                res = arrays[0];
            }else {
                res = Arrays.toString(arrays);
            }
        }
        return res;
    }

}
