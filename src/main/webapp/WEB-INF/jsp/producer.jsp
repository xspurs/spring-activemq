<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>生产者页面</title>
    <script src="${pageContext.request.contextPath}/static/login/js/jquery-1.11.2.min.js" type="text/javascript"></script>
    <script>
        $(function() {
            $('#commit-btn').on('click', function(e) {
                var inputVal = $('#input-area').val()
                $.ajax({
                    url: 'producer/message/' + inputVal,
                    success: function(response) {
                        // 内容回显
                        $('#dashboard').text(response)
                    }
                })
            });
        })
    </script>
</head>
<body>
    <!-- 内容展示区 -->
    <div id="dashboard">
        请发送消息
    </div>

    <div id="action-area">
        <input id="input-area" type="text" placeholder="请输入要发送的消息">
        <br>
        <button id="commit-btn">发送</button>
    </div>
</body>
</html>
