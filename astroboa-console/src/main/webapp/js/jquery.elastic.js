(function(g){g.fn.extend({elastic:function(){var h=["paddingTop","paddingRight","paddingBottom","paddingLeft","fontSize","lineHeight","fontFamily","width","fontWeight"];return this.each(function(){function i(c,j){curratedHeight=Math.floor(parseInt(c,10));a.height()!=curratedHeight&&a.css({height:curratedHeight+"px",overflow:j})}function k(){var c=a.val().replace(/&/g,"&amp;").replace(/ /g,"&nbsp;").replace(/<|>/g,"&gt;").replace(/\n/g,"<br />"),j=b.html().replace(/<br>/ig,"<br />");if(c+"&nbsp;"!= j){b.html(c+"&nbsp;");if(Math.abs(b.height()+l-a.height())>3){c=b.height()+l;if(c>=d)i(d,"auto");else c<=e?i(e,"hidden"):i(c,"hidden")}}}if(this.type!="textarea")return false;var a=g(this),b=g("<div />").css({position:"absolute",display:"none","word-wrap":"break-word"}),l=parseInt(a.css("line-height"),10)||parseInt(a.css("font-size"),"10"),e=parseInt(a.css("height"),10)||l*3,d=parseInt(a.css("max-height"),10)||Number.MAX_VALUE,f=0;if(d<0)d=Number.MAX_VALUE;b.appendTo(a.parent());for(f=h.length;f--;)b.css(h[f].toString(), a.css(h[f].toString()));a.css({overflow:"hidden"});a.bind("keyup change cut paste",function(){k()});a.bind("blur",function(){if(b.height()<d)b.height()>e?a.height(b.height()):a.height(e)});a.live("input paste",function(){setTimeout(k,250)});k()})}})})(jQuery);