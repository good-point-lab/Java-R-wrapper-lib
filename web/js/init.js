if (typeof pathPrefix == 'undefined' ) pathPrefix = '../';

$.template("liTemplate", '<li>{{if url}}<a href="{{if prefix}}__prefixPath__{{/if}}${url}" class="${cssclass}">{{/if}}${title}{{if url}}</a>{{/if}}</li>' );
var sectionTemplate = $.template('<ul class="${cssclass}">{{tmpl(listItems) "liTemplate"}}</ul>');


$(document).ready(function() {
    
    $('#pageContent').loadMarkdown({
         url: "README.md",
         replaceWith: true,
         preAdd: function(result) {
            result = result.replace(/<pre><code>/g,'<pre class="prettyprint">');
            result = result.replace(/<\/code><\/pre>/g,'</pre>');
            return result.replace(/__prefixPath__/g, pathPrefix);  
         },
         postAdd: function() {
             prettyPrint();
         }
    });
    
    
    $.getJSON(pathPrefix + 'assets/shared/toolbar.json', function(json) {
        var result = $('<div />');
        $.tmpl(sectionTemplate, json).appendTo(result);
        $('#toolbarLinks').replaceWith(result.html().replace(/__prefixPath__/g, pathPrefix));
    });
    
    $.getJSON(pathPrefix + 'assets/shared/sidebar.json', function(json) {
        var result = $('<div />');
        $.tmpl(sectionTemplate, json).appendTo(result);
        
		$('#sideNavLinks').replaceWith(result.html().replace(/__prefixPath__/g, pathPrefix));
	});
    
        
    //alert(window.location.pathname);
});