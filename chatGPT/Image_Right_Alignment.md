**Paul Carlton**: Given following html how do I get image right aligned <tbody><tr style="">
    <td style="padding:7.5pt 15.0pt 7.5pt 15.0pt"><p class="x_MsoNormal">
    <span style="font-family:Arial;,sans-serif">
        <a data-auth="NotApplicable" rel="noopener noreferrer"
           target="_blank" href="http://click.mail.tesco.com/CL0/http:%2F%2Fclick.mail-ppe.tesco.com%2FCL0%2Fhttp:%252F%252Fwww.tesco.com%2F1%2F01020169b9800a30-1c28a36c-9cdd-40d2-a4e3-78efc1bd7d56-000000%2F6Hks3QD5No99X3ok5aYBNsMIfNZZS6Luil2_v-hQPrM=81/1/01020194c6482266-4d0490ac-2845-4ae0-85be-902414080add-000000/saGyIPhknSuyIDnLKptuTjlhIpptremR5Mrt-IXN1GY=390"
           title="http://click.mail.tesco.com/CL0/http:%2F%2Fclick.mail-ppe.tesco.com%2FCL0%2Fhttp:%252F%252Fwww.tesco.com%2F1%2F01020169b9800a30-1c28a36c-9cdd-40d2-a4e3-78efc1bd7d56-000000%2F6Hks3QD5No99X3ok5aYBNsMIfNZZS6Luil2_v-hQPrM=81/1/01020194c6482266-4d0490ac-2845-4ae0-85be-902414080add-000000/saGyIPhknSuyIDnLKptuTjlhIpptremR5Mrt-IXN1GY=390"
           data-linkindex="1">
            <span style="text-decoration:none">
            <img style="box-sizing:border-box; border-bottom-style:none; border-left-style:none; border-right-style:none; border-top-style:none; padding-bottom:0in; padding-left:0in; padding-right:0in; padding-top:0in"
                 align="right" alt="Tesco logo" id="x__x0000_i1025" height="33" width="120" border="0"
                 src="https://digitalcontent.api.tesco.com/v2/media/emails/ffbee7f9-65ce-4adf-bbca-2162b8963256/original.png"
                 data-imagetype="External">
        </span>
        </a>
    </span>
    </p>
    </td>
</tr>
</tbody>

**ChatGPT**: Your image already has `align="right"`, but `align` is an outdated attribute and may not work in some modern email clients. Instead, you can use CSS to ensure right alignment.

### Solution:
Modify the `img` tag to include `style="float: right;"`, like this:

```html
<img style="float: right; box-sizing: border-box; border: none; padding: 0;"
     alt="Tesco logo" height="33" width="120" border="0"
     src="https://digitalcontent.api.tesco.com/v2/media/emails/ffbee7f9-65ce-4adf-bbca-2162b8963256/original.png">
```

### Alternative using `td`:
If you want to ensure proper alignment across email clients, you can wrap the image inside a table cell and set `align="right"` at the `td` level:

```html
<tr>
    <td align="right" style="padding:7.5pt 15.0pt;">
        <a href="http://www.tesco.com">
            <img src="https://digitalcontent.api.tesco.com/v2/media/emails/ffbee7f9-65ce-4adf-bbca-2162b8963256/original.png"
                 alt="Tesco logo" height="33" width="120" border="0"
                 style="border: none; display: block;">
        </a>
    </td>
</tr>
```

This method ensures better compatibility with email clients, as many do not fully support floating elements.

