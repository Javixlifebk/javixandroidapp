<?php

ob_start();
error_reporting(E_ALL);
$strNo = rand(1,1000000);

date_default_timezone_set('Asia/Calcutta');

//echo date_default_timezone_get();

$strCurDate = date('d-m-Y');

require_once 'TransactionRequestBean.php';
require_once 'TransactionResponseBean.php';

session_start();

if($_POST && isset($_POST['submit'])){

    $val = $_POST;

    $_SESSION['iv'] = $val['iv'];
    $_SESSION['key']   = $val['key'];

    $transactionRequestBean = new TransactionRequestBean();

    //Setting all values here
    //$transactionRequestBean->setMerchantCode($val['mrctCode']);
    $transactionRequestBean->merchantCode = $val['mrctCode'];
    $transactionRequestBean->accountNo = $val['tpvAccntNo'];
    $transactionRequestBean->ITC = $val['itc'];
    $transactionRequestBean->mobileNumber = $val['mobNo'];
    $transactionRequestBean->customerName = $val['custname'];
    $transactionRequestBean->requestType = $val['reqType'];
    $transactionRequestBean->merchantTxnRefNumber = $val['mrctTxtID'];
    $transactionRequestBean->amount = $val['amount'];
    $transactionRequestBean->currencyCode = $val['currencyType'];
    $transactionRequestBean->returnURL = $val['returnURL'];
    $transactionRequestBean->s2SReturnURL = $val['s2SReturnURL'];
    $transactionRequestBean->shoppingCartDetails = $val['reqDetail'];
    $transactionRequestBean->txnDate = $val['txnDate'];
    $transactionRequestBean->bankCode = $val['bankCode'];
    $transactionRequestBean->TPSLTxnID = $val['tpsl_txn_id'];
    $transactionRequestBean->custId = $val['custID'];
    $transactionRequestBean->cardId = $val['cardID'];
    $transactionRequestBean->key = $val['key'];
    $transactionRequestBean->iv = $val['iv'];
    $transactionRequestBean->webServiceLocator = $val['locatorURL'];
    $transactionRequestBean->MMID = $val['mmid'];
    $transactionRequestBean->OTP = $val['otp'];
    $transactionRequestBean->cardName = $val['cardName'];
    $transactionRequestBean->cardNo = $val['cardNo'];
    $transactionRequestBean->cardCVV = $val['cardCVV'];
    $transactionRequestBean->cardExpMM = $val['cardExpMM'];
    $transactionRequestBean->cardExpYY = $val['cardExpYY'];
    $transactionRequestBean->timeOut = (!empty($val['timeOut']) ? $val['timeOut'] : 30 );

    $url = $transactionRequestBean->getTransactionToken();

    $responseDetails = $transactionRequestBean->getTransactionToken();
    $responseDetails = (array)$responseDetails;
    $response = $responseDetails[0];

    if(is_string($response) && preg_match('/^msg=/',$response)){
        $outputStr = str_replace('msg=', '', $response);
        $outputArr = explode('&', $outputStr);
        $str = $outputArr[0];

        $transactionResponseBean = new TransactionResponseBean();
        $transactionResponseBean->setResponsePayload($str);
        $transactionResponseBean->setKey($val['key']);
        $transactionResponseBean->setIv($val['iv']);

        $response = $transactionResponseBean->getResponsePayload();
        echo "<pre>";
        print_r($response);
        exit;
    }elseif(is_string($response) && preg_match('/^txn_status=/',$response)){
		echo "<pre>";
        print_r($response);
        exit;
	}

    echo "<script>window.location = '".$response."'</script>";
    ob_flush();

}else if($_POST){

    $response = $_POST;

    if(is_array($response)){
        $str = $response['msg'];
    }else if(is_string($response) && strstr($response, 'msg=')){
        $outputStr = str_replace('msg=', '', $response);
        $outputArr = explode('&', $outputStr);
        $str = $outputArr[0];
    }else {
        $str = $response;
    }

    $transactionResponseBean = new TransactionResponseBean();

    $transactionResponseBean->setResponsePayload($str);
    $transactionResponseBean->key = $_SESSION['key'];
    $transactionResponseBean->iv = $_SESSION['iv'];

    $response = $transactionResponseBean->getResponsePayload();
    echo "<pre>";
    print_r($response);
    echo "<br><br><br><br>";

    session_destroy();?>

    <a href='<?php echo "http://".$_SERVER["HTTP_HOST"].$_SERVER['SCRIPT_NAME'];?>'>GO TO HOME</a>

    <?php
    exit;
}

?>

<html>
<body>
<form method="post" name="techprocess">
	<table class="tbl" width="60%" border="1" cellpadding="2" cellspacing="0">
	<tr>
    	<th width="40%">Field Description</th>
        <th width="20%">Field Name</th>
    </tr>
    <tr>
    	<td><label>Request Type</label>o</td>
        <td><select name="reqType">
				<option value="T">T</option>
        	</select>
        </td>
    </tr>
    <tr>
    	<td><label>Merchant Code</label></td>
        <td><input type="text" name="mrctCode" value="T3348"/></td>
    </tr>
	<tr>
    	<td><label>Merchant Transaction ID</label></td>
        <td><input type="text" name="mrctTxtID" value="<?php echo $strNo; ?>"/></td>
    </tr>
     <tr>
    	<td><label>Currency Code</label></td>
        <td><input type="text" name="currencyType" value="INR"/></td>
    </tr>
    <tr>
    	<td><label>Amount</label></td>
        <td><input type="text" name="amount" onChange="updateReqDetail()" value="1.00"/></td>
    </tr>
    <tr>
    	<td><label>Client Meta Data</label></td>
        <td><input type="text" name="itc" value="NIC~TXN0001~122333~rt14154~8 mar 2014~Payment~forpayment"/></td>
    </tr>
    <tr>
    	<td><label>Request Detail</label></td>
        <td><input type="text" name="reqDetail" value="FIRST_1.0_0.0"/></td>
    </tr>
     <tr>
    	<td><label>Transaction Date</label></td>
        <td><input type="text" name="txnDate" value="<?php echo $strCurDate;?>"/></td>
    </tr>
    <tr>
    	<td><label>Bank Code</label></td>
        <td><input type="text" name="bankCode" value="470"/></td>
    </tr>
     <tr>
    	<td><label>Locator URL</label></td>
        <td><select name="locatorURL">
	        	<option selected value="https://payments.paynimo.com/PaynimoProxy/services/TransactionLiveDetails?wsdl">LIVE</option>
        	</select>
        </td>
    </tr>
	<tr>
    	<td><label>S2S URL </label></td>
        <td>
	       <input type="text" name="s2SReturnURL" value="https://tpslvksrv6046/LoginModule/Test.jsp"/>
		</td>
    </tr>
    <tr>
    	<td><label>TPSL Transaction ID</label></td>
        <td><input type="text" name="tpsl_txn_id" value="<?php echo 'TXN00'.rand(1,10000); ?>"/></td>
    </tr>
	 <tr>
    	<td><label>Card ID</label></td>
        <td><input type="text" name="cardID" value=""/></td>
    </tr>
    <tr>
    	<td><label>Customer ID</label></td>
        <td><input type="text" name="custID" value="19872627"/></td>
    </tr>
    <tr>
    	<td><label>Customer Name</label></td>
        <td><input type="text" name="custname" value="test"/></td>
    </tr>
	<tr>
    	<td><label>Timeout</label></td>
        <td><input type="text" name="timeOut" value=""/></td>
    </tr>
	<tr>
    	<td><label>Mobile Number</label></td>
        <td><input type="text" name="mobNo" value=""/></td>
    </tr>
	<tr>
    	<td><label>Account Number</label></td>
        <td><input type="text" name="accNo" value=""/></td>
    </tr>
	<tr>
    	<td><label>Tpv Account Number</label></td>
        <td><input type="text" name="tpvAccntNo" value=""/></td>
    </tr>
    <tr>
    	<td><label>MMID</label></td>
        <td><input type="text" name="mmid" value=""/></td>
    </tr>
	<tr>
    	<td><label>OTP</label></td>
        <td><input type="text" name="otp" value=""/></td>
    </tr>
	<tr>
    	<td><label>Transaction Type</label></td>
        <td><input type="text" name="TxnType" value=""/></td>
    </tr>
	<tr>
    	<td><label>Transaction SubType</label></td>
        <td><input type="text" name="TxnSubType" value=""/></td>
    </tr>
	<tr>
    	<td><label>Card name</label></td>
        <td><input type="text" name="cardName" value=""/></td>
    </tr>
	<tr>
    	<td><label>Card Number</label></td>
        <td><input type="text" name="cardNo" value=""/></td>
    </tr>
	<tr>
    	<td><label>Card CVV Number</label></td>
        <td><input type="text" name="cardCVV" value=""/></td>
    </tr>
	<tr>
    	<td><label>Card Exp MM</label></td>
        <td><input type="text" name="cardExpMM" value=""/></td>
    </tr>
	<tr>
    	<td><label>Card Exp YY</label></td>
        <td><input type="text" name="cardExpYY" value=""/></td>
    </tr>
	<tr>
    	<td><label>Key</label></td>
        <td><input type="text" name="key" value="1496899267KMOWJE"/></td>
    </tr>
	<tr>
    	<td><label>IV</label></td>
        <td><input type="text" name="iv" value="4782583707RRHRBR"/></td>
    </tr>
	<tr>
    	<td><label>Return URL </label></td>
        <td>
	        <input type="text" name="returnURL" value='<?php echo "http://".$_SERVER["HTTP_HOST"].$_SERVER['SCRIPT_NAME'];?>'/>
		</td>
    </tr>
	<tr>
        <td colspan=2>
         <input type="submit" name="submit" value="Submit" />
        </td>
	</tr>
</table>
</form>
<script type="text/javascript">
	function updateReqDetail() {
		var amount = document.techprocess.amount.value;
		var reqDetail = "FIRST_" + amount + '_0.0';
		document.techprocess.reqDetail.value = reqDetail;
	}
</script>
</body>
</html>