$(function () {
    function initDateForm() {
        var opt = {};
        opt.date = {preset: 'date'};
        opt.datetime = {
            preset: 'datetime',
            minDate: new Date(2012, 3, 10, 9, 22),
            maxDate: new Date(2014, 7, 30, 15, 44),
            stepMinute: 5
        };
        opt.time = {preset: 'time'};
        opt.tree_list = {preset: 'list', labels: ['Region', 'Country', 'City']};
        opt.image_text = {preset: 'list', labels: ['Cars']};
        opt.select = {preset: 'select'};
        <!--Script-->
        $('select.changes').bind('change', function () {
            var demo = $('#demo').val();
            $(".demos").hide();
            if (!($("#demo_" + demo).length))
                demo = 'default';
            $("#demo_" + demo).show();
            $('#test_' + demo).val('').scroller('destroy').scroller($.extend(opt[$('#demo').val()],
                {
                    theme: 'android-ics light',
                    mode: $('#mode').val(),
                    display: $('#display').val(),
                    dateFormat: 'yy-mm-dd',
                    setText: '确定', //确认按钮名称
                    cancelText: '清空',//取消按钮名籍我
                }));
        });
        $('#demo').trigger('change');
    }

    function init() {
        initDateForm();
    }

    init();
});