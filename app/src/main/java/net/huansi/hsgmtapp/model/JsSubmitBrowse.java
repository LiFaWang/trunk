package net.huansi.hsgmtapp.model;

import huansi.net.qianjingapp.entity.WsData;

/**
 * Created by Administrator on 2016/4/22 0022.
 */
public class JsSubmitBrowse extends WsData {
    /*
    *             "IHDRIDEN": "4",
            "DHDRDATE": "12-20",
            "DHDRTIME": "11:57",
            "SHDRTITLE": "",
            "SHDRCONTENTS": "",
            "IDTLIDEN": "3",
            "IDTLSEQ": "0",
            "SDTLFILETYPE": "IMAGE",
            "SDTLLOCALFILEPATH": "/storage/emulated/0/tencent/micromsg/weixin/microMsg.1482156929179.jpg",
            "SDTLFILEPATH": "HsFiles/201612201157445813.jpg",
            "SDTLTHUMBFILEPATH": "HsFiles/201612201157445813_thumb.jpg",
            "BDTLALLOWDELETE": "0"
    * */




    /**
     *头表标识
     */
    public String IHDRIDEN;
    /**
     *日期
     */
    public String DHDRDATE;
    /**
     *时间
     */
    public String DHDRTIME;
    /**
     *主题
     */
    public String SHDRTITLE;
    /**
     *内容
     */
    public String SHDRCONTENTS;



    /**
     *明细标识
     */
    public String IDTLIDEN;
    /**
     *附件的顺序号
     */
    public String IDTLSEQ;
    /**
     *附件类型
     */
    public String SDTLFILETYPE;
    /**
     *本地文件地址
     */
    public String SDTLLOCALFILEPATH;
    /**
     *大图地址；需要在前面加上"http://hsapp.huansi.net/"
     */
    public String SDTLFILEPATH;
    /**
     *缩略图地址：需要在前面加上"http://hsapp.huansi.net/"
     */
    public String SDTLTHUMBFILEPATH;
    /**
     *是否允许删除；0不允许，1允许；
     */
    public String BDTLALLOWDELETE;
//    /**
//     * 附件信息
//     */
//    public String SFILEDESC;
}
