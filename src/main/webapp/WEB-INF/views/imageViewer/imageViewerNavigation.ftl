<nav class="global">
    <div class="navigation_btn"><img src="${rc.getContextPath()}/resources/images/global/btn.png" alt="btn" width="60"
                          height="60"
    /></div>
    <ul>

                   <li class="icon_01">
              <div class="name">ツール</div>
              <ul>
                <li class="icon_01_01" id="select">選択</li>
                <li class="icon_01_02">印刷
                  <ul>
                    <li id="print">全てを印刷</li>
                    <li id="print0">アノテーションを印刷しない</li>
                  </ul>
                </li>
                <li class="icon_01_03" id="subImage">サブイメージビューアー</li>
              </ul>
            </li>
            <li class="icon_02">
              <div class="name">ズーム</div>
              <ul>
                <li class="icon_02_01">ズーム
                  <ul>
                    <li id="zoomFullPage">全表示</li>
                    <li id="zoomFullWidth">幅に合わせる</li>
                    <li id="zoom200">200%</li>
                    <li id="zoom100">100%</li>
                    <li id="zoom75">75%</li>
                    <li id="zoom50">50%</li>
                      <li class="inputHover"><input type="number" id="zoomInput" value="100"></li>
                  </ul>
                </li>
                <li class="icon_02_02" id="zoomin">ズームイン</li>
                <li class="icon_02_03" id="zoomout">ズームアウト</li>
              </ul>
            </li>
            <li class="icon_03">
              <div class="name">アノテーション</div>
              <ul>
                <li class="icon_03_01">アノテーション作成
                  <ul>
                    <li id="line"><a href="#">線</a></li>
                    <li id="rectangle"><a href="#">シェイプ</a></li>
                    <li id="text"><a href="#">文字フィールド</a></li>
                    <li id="highlight"><a href="#">ハイライト</a></li>
                    <li id="comment"><a href="#">コメント</a></li>
                  </ul>
                </li>
                <li class="link icon_03_02" id="properties"><a href="#">プロパティ</a></li>
                <li class="link icon_03_03" id="btnGrayscale"><a href="#">グレースケール</a></li>
                <li class="link icon_03_04" id="btnLayer"><a href="#">レイヤー</a></li>
              </ul>
            </li>
            <li class="icon_04">
              <div class="name">編集</div>
              <ul>
                <li class="icon_04_01" id="cut">カット</li>
                <li class="icon_04_02" id="copy">コピー</li>
                <li class="icon_04_03" id="paste">ペースト</li>
                <li class="icon_04_04" id="rotateC">反時計回り90°回転</li>
                <li class="icon_04_05" id="rotate">時計回り90°回転</li>
              </ul>
            </li>
            <li class="icon_05">
              <div class="name">イメージ表示</div>
              <ul>
                <li class="icon_05_01" id="first">先頭のイメージ</li>
                <li class="icon_05_02" id="previous">前のイメージ</li>
                <li class="icon_05_03" id="next">次のイメージ</li>
                <li class="icon_05_04" id="last">最後のイメージ</li>
              </ul>
            </li>
            <li class="icon_06">
              <div class="name">ページ</div>
              <ul>
                <li id="chooseImage1" class="inputHover">
                    <input type="text" name="pageInput"
                           id="pageInput">
                  <ul id="chooseImage">

                  </ul>
                </li>
              </ul>
            </li>
    </ul>
</nav>

<!-- Modal -->
<div class="modal fade" id="myModalText" role="dialog" z-index='302'>
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header imageViewerPopup">

                <h4 class="modal-title">文字フィールドプロパティ</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <textarea class="form-control" rows="5" id="commentText"></textarea>
                </div>
                <div class="form-group">
                    <div class="col-md-4">
                    Pen Color: </div><div class="col-md-8"><select id="color"><option value="rgb(0, 0, 0)
">Black</option>
                        <option value="rgb(255, 0, 0)">Red</option></select></div>
                </div>
                <div class="form-group"><div class="col-md-4">
                    Fill Color: </div><div class="col-md-8"><select id="fill"><option
                            value="rgba(0, 0, 0, 0)">Transparent</option>
                        <option value="rgb(0, 0, 0)">Black</option></select></div>
                </div>
                <div class="form-group"><div class="col-md-4">
                    Text Border Color: </div><div class="col-md-8"><select id="border"><option
                            value="1px solid rgba(0, 0, 0, 0)">Transparent</option>
                        <option value="1px solid rgb(0, 0, 0)">Black</option></select></div>
                </div>
                <div class="form-group"><div class="col-md-4">
                    Font: </div><div class="col-md-8"><select id="font"><option value="serif">Serif</option>
                        <option value="Arial">Arial</option>
                        <option value=\ "Times NewRoman"\>Times New Roman</option></select></div>
                </div>
                <div class="form-group"><div class="col-md-4">
                    Font Size: </div><div class="col-md-8"><select id="fontSize"><option value="18px">18</option>
                        <option value="20px">20</option></select></div>
                </div>
                <div class="form-group"><div class="col-md-4">
                    Font Style: </div><div class="col-md-8"><select id="fontStyle"><option value="normal">Regular</option>
                        <option value="bold">Bold</option>
                        <option value="italic">Italic</option>
                        <option value="boldItalic">Bold Italic</option></select></div>
                </div>
            </div>
            <div class="modalTextProperties">
                <button type="button" class="btn btn-default btn_cancel_imageViewer btn_popup" data-dismiss="modal"
                        id="cancelTextProperties">キャンセル</button>
                <button type="button" class="btn btn-default btn_save_imageViewer btn_popup" data-dismiss="modal"
                        id="textProperties">確認</button>
            </div>
        </div>
    </div>
</div>
<!-- Modal -->
<div class="modal fade" id="myModalComment" role="dialog" z-index='302'>
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header imageViewerPopup">

                <h4 class="modal-title">コメントプロパティ</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <textarea class="form-control" rows="5" id="commentTxtArea"></textarea>
                </div>
                <div class="form-group">
                Label <input type="text" id="tbComment" value="" style="width: 60%">
                </div>
            </div>
            <div class="modalCommentProperties">

                <button type="button" class="btn btn-default btn_cancel_imageViewer btn_popup" data-dismiss="modal"
                        id="cancelCommentProperties">キャンセル</button>
                <button type="button" class="btn btn-default btn_save_imageViewer btn_popup" data-dismiss="modal"
                        id="commentProperties">確認</button>
            </div>
        </div>
    </div>
</div>
<!-- Modal -->
<div class="modal fade" id="myModalLine" role="dialog" z-index='302'>
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header imageViewerPopup">

                <h4 class="modal-title">アノテーションプロパティ</h4>
            </div>
            <div class="modal-body">
                <div class="col-md-8">
                    カラー
                    <ul class="nav nav-tabs">
                        <li><a data-toggle="tab" href="#home" id="li1">線</a></li>
                        <li><a data-toggle="tab" href="#menu1" id="li2">シェイプ</a></li>
                        <li><a data-toggle="tab" href="#menu2" id="li3">ハイライト</a></li>
                    </ul>
                    <div class="tab-content">
                        <div id="home" class="tab-pane fade in active">
                            <input type="radio" name="color" id="black" value="black" checked />
                            <label for="black"><span class="black"></span></label><input
                                type="radio" name="color" id="red" value="red" /> <label
                                for="red"><span class="red"></span></label> <input type="radio"
                                name="color" id="green" value="green" /> <label for="green"><span
                                class="green"></span></label> <input type="radio" name="color"
                                id="yellow" value="yellow" /> <label for="yellow"><span
                                class="yellow"></span></label> <input type="radio" name="color"
                                id="olive" value="olive" /> <label for="olive"><span
                                class="olive"></span></label> <input type="radio" name="color"
                                id="orange" value="orange" /> <label for="orange"><span
                                class="orange"></span></label> <input type="radio" name="color"
                                id="teal" value="teal" /> <label for="teal"><span
                                class="teal"></span></label> <input type="radio" name="color" id="blue"
                                value="blue" /> <label for="blue"><span class="blue"></span></label><input
                                type="radio" name="color" id="violet" value="violet" /><label
                                for="violet"><span class="violet"></span></label> <input
                                type="radio" name="color" id="none" value="none" /> <label
                                for="none"><span class="none"></span></label>
                        </div>
                        <div id="menu1" class="tab-pane fade">

                            <input type="radio" name="fill" id="1black" value="black" /> <label
                                for="black"><span class="black"></span></label> <input
                                type="radio" name="fill" id="1red" value="red" /> <label
                                for="red"><span class="red"></span></label> <input type="radio"
                                name="fill" id="1green" value="green" /> <label for="green"><span
                                class="green"></span></label> <input type="radio" name="fill"
                                id="1yellow" value="yellow" /> <label for="yellow"><span
                                class="yellow"></span></label> <input type="radio" name="fill"
                                id="1olive" value="olive" /> <label for="olive"><span
                                class="olive"></span></label> <input type="radio" name="fill"
                                id="1orange" value="orange" /> <label for="orange"><span
                                class="orange"></span></label> <input type="radio" name="fill"
                                id="1teal" value="teal" /> <label for="teal"><span
                                class="teal"></span></label> <input type="radio" name="fill" id="1blue"
                                value="blue" /> <label for="blue"><span class="blue"></span></label><input
                                type="radio" name="fill" id="1violet" value="violet" /><label
                                for="violet"><span class="violet"></span></label> <input
                                type="radio" name="fill" id="1none" value="none" /> <label
                                for="none"><span class="none"></span></label>
                        </div>
                        <div id="menu2" class="tab-pane fade">

                            <input type="radio" name="highlight" id="2black" value="black" /><label
                                for="black"><span class="black"></span></label> <input
                                type="radio" name="highlight" id="2red" value="red" /> <label
                                for="red"><span class="red"></span></label> <input type="radio"
                                name="highlight" id="2green" value="green" /> <label
                                for="green"><span class="green"></span></label> <input
                                type="radio" name="highlight" id="2yellow" value="yellow" /> <label
                                for="yellow"><span class="yellow"></span></label> <input
                                type="radio" name="highlight" id="2olive" value="olive" /> <label
                                for="olive"><span class="olive"></span></label> <input
                                type="radio" name="highlight" id="2orange" value="orange" /> <label
                                for="orange"><span class="orange"></span></label> <input
                                type="radio" name="highlight" id="2teal" value="teal" /> <label
                                for="teal"><span class="teal"></span></label> <input
                                type="radio" name="highlight" id="2blue" value="blue" /> <label
                                for="blue"><span class="blue"></span></label> <input
                                type="radio" name="highlight" id="2violet" value="violet" /> <label
                                for="violet"><span class="violet"></span></label> <input
                                type="radio" name="highlight" id="2purple" value="purple" /> <label
                                for="purple"><span class="purple"></span></label>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    ラインサイズ <br /> <input type="number" min="1" max="100" value="1"
                        id="width">

                </div>

            </div>
            <div class="modalProperties">
                <button type="button" class="btn btn-default btn_cancel_imageViewer btn_popup" data-dismiss="modal"
                        id="cancelProperties">キャンセル</button>
                <button type="button" class="btn btn-default btn_save_imageViewer btn_popup" data-dismiss="modal"
                        id="okProperties">確認</button>
            </div>

        </div>
    </div>
</div>
<!-- Modal -->
<div class="modal fade" id="myModal2" role="dialog" z-index='302'>
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header imageViewerPopup">

                <h4 class="modal-title imageViewerTitle">グレースケール</h4>
            </div>
            <div class="modal-body">
                <div>
                    <label class="imageViewerLabel">コントラスト</label> <input type="text" name="contrastValue"
                        id="contrastValue" style="float: right; text-align: center;" class="imageViewerInput">
                    <input type="range" id="controls1" min="-100" max="100" class="rangeImageViewer">
                </div>
                <div>
                    <label class="imageViewerLabel">プライトネス</label> <input type="text" name="brightnessValue"
                        id="brightnessValue" style="float: right; text-align: center;" class="imageViewerInput">
                    <input type="range" id="controls" min="-100" max="100" class="rangeImageViewer">
                </div>
            </div>
            <div class="modal-footer">
                <label style="display: block; text-align: left;" class="imageViewerLabel"><input
                    type="checkbox" id="cbox1" value="grayscale"> グレースケール</label>
                <button type="button" class="btn btn-default btn_cancel_imageViewer btn_popup" data-dismiss="modal" id="cancelGrayscale">キャンセル</button>
                <button type="button" class="btn btn-default btn_save_imageViewer btn_popup" data-dismiss="modal"
                    id="okGrayscale">確認</button>

            </div>
        </div>
    </div>
</div>
<!-- Modal -->
<div class="modal fade" id="myModal3" role="dialog" z-index='302'>
    <div class="modal-dialog content_main">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header imageViewerPopup">

                <h4 class="modal-title imageViewerTitle">レイヤー管理画面</h4>
            </div>
            <div class="modal-body" id="layerBody">
                <div class="form-inline">
                    <div class="form-group ">

                            名前
                            <div class="form-group"><input type="text" id="layerName"
                                   name="layerName" class="form-control"></div>



                            アクティブ <div class="form-group"><input type="text"
                                id="layerActive" name="layerActive" class="form-control" style="background-color: #fff"
                                disabled></div>
                        <button type="button" class="btn btn-default btn_imageViewer btn_popup"
                                id="addLayer">追加</button>
                        <button type="button" class="btn btn-default btn_imageViewer btn_popup" id="removeLayer">移動</button>
                        <button type="button" class="btn btn-default btn_imageViewer btn_popup" id="renameLayer">名前変更</button>
                        <button type="button" class="btn btn-default btn_imageViewer btn_popup" id="activeLayer">活性化</button>
                        <button type="button" class="btn btn-default btn_imageViewer btn_popup" id="displayLayer">表示</button>

                    </div>
                </div>

                    <table class="table table-bordered tableImageViewer">
                        <thead>
                        <tr>
                            <th style="background-color: #F2F2F2">名前</th>
                            <th style="background-color: #D9D9D9">ユーザ</th>
                            <th style="background-color: #F2F2F2">表示</th>
                            <th style="background-color: #D9D9D9">アイテム</th>
                            <th style="background-color: #F2F2F2">修正日時</th>
                            <th style="background-color: #D9D9D9">作成日時</th>
                            <th style="background-color: #F2F2F2">活性化</th>
                            <th style="background-color: #D9D9D9">テンプレート名</th>
                        </tr>
                        </thead>
                        <tbody id="newLayerBody">

                        </tbody>
                    </table>
                <div class=" text-center">
                <button type="button" class="btn btn-default btn_cancel_imageViewer btn_popup" data-dismiss="modal">キャンセル</button>
                <button type="button" class="btn btn-default btn_save_imageViewer btn_popup" data-dismiss="modal"
                        id="okLayer">確認</button></div>
            </div>

        </div>
    </div>
</div>
<!-- Modal -->
<div class="modal fade" id="myModalConfirm" role="dialog" z-index='302'>
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header imageViewerPopup">

                <h4 class="modal-title imageViewerTitle">Delete layer?</h4>
            </div>
            <div class="modal-body" id="layerBody">
                <div class="form-inline">
                    <div class="form-group ">

                        Layer have items. Delete anyway?

                    </div>
                </div>


                <div class=" text-center">
                    <button type="button" class="btn btn-default btn_cancel_imageViewer btn_popup"
                            data-dismiss="modal">No</button>
                    <button type="button" class="btn btn-default btn_save_imageViewer btn_popup" data-dismiss="modal"
                            id="okLayerDelete">Yes</button></div>
            </div>

        </div>
    </div>
</div>
<script id="layer-row-template" type="text/x-handlebars-template">
    <tr>
        <td>
            <div id="layer{{countLayer}}">
                <input type="checkbox" name="layerName" value="{{layerName}}" style="margin-top: 0px">
                {{layerName}}
            </div>
        </td>
        <td>
            {{layerOwner}}
        </td>
        <td style="text-align: center;">
            <input type="checkbox" name="display" value="{{layerName}}" {{#if layerDisplay}} checked{{/if}}/>
        </td>
        <td style="text-align: right;">
            {{layerItems}}
        </td>
        <td>
            {{outputModified}}
        </td>
        <td>
            {{outputCreated}}
        </td>
        <td style="text-align: center;">
            <input type="radio" name="active" value="{{layerName}}" {{#if layerActive}} checked{{/if}}/>
        </td>
        <td>
            {{templateName}}
        </td>
    </tr>

</script>