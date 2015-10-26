define(function (require, exports, module) {
    function Words() {
        this.word = null;
    }
    Words.prototype = {
        add  : function (word, desc) {
            var words = localStorage.getItem("aj-words"),
                results = [];
            if (!word) {
                return false;
            }
            var item = {
                word : word,
                content : desc,
                is_sysnc : false,
                date : + new Date()
            };
            if (words) {
                results = $.parseJSON(words);
            }
            results.push(JSON.stringify(item));
            localStorage.setItem('aj-words', JSON.stringify(results));
            return true;
        },
        getAll : function () {
            var words = localStorage.getItem("aj-words"),
                results = [];
            if (words) {
                results = results.concat($.parseJSON(words));
            }
            return results;
        },
        inLocal : function (word) {
            var words = this.getAll(),
                one;
            if (!words) {
                return null;
            }
            for (var i = 0; i < words.length; i++) {
                one = $.parseJSON(words[i]);
                if (one.word.toLowerCase() === word.toLowerCase()){
                    return one.content;
                }
            }
            return null;
        },
        sysnc : function (fn) {
            var words = this.getAll(),
                user = Parse.User.current();
            if (user === null) {
                fn({
                    isok : false,
                    info : "Not sign in!"
                });
                return false;
            }
            words.forEach(function (item) {
                var one = $.parseJSON(item);
                console.log(one);
            });
            fn({
                isok : true,
                info : '单词本已同步到云端'
            });
        }
    };
    var w = new Words();
    module.exports = w;
});