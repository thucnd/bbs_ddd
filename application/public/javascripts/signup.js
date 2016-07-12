$(document).ready(function () {
    $('input[type=radio][name=companyType]').change(function () {
        $("#email-format").empty();
        if (this.value === "1") {
            $("#email-format").append("septeni-technology.jp");
        } else {
            $("#email-format").append("septeni-original.co.jp");
        }
    });
});