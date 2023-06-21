<?php

require_once 'RequestValidate.php';
require_once 'AES.php';

/**
 * Version 1.0
 */
class TransactionResponseBean extends RequestValidate
{

    protected $responsePayload = "";

    protected $key;

    protected $iv;

    protected $logPath = "";

    protected $blocksize = 128;

    protected $mode = "cbc";

    /**
     * @param set all data in variables
     */
    public function __set($field,$value)
    {
        $this->$field = $value;
    }

    /**
     * @param get all data in variables
     */
    public function __get($variable)
    {
        return $this->variable;
    }

	/**
     * @return the $logPath
     */
    public function getLogPath()
    {
        return $this->logPath;
    }

	/**
     * @return the $blocksize
     */
    public function getBlocksize()
    {
        return $this->blocksize;
    }

	/**
     * @return the $mode
     */
    public function getMode()
    {
        return $this->mode;
    }

	/**
     * @param number $responsePayload
     */
    public function setResponsePayload($responsePayload)
    {
        $this->responsePayload = $responsePayload;
    }

	/**
     * @param string $logPath
     */
    public function setLogPath($logPath)
    {
        $this->logPath = $logPath;
    }

	/**
     * @param number $blocksize
     */
    public function setBlocksize($blocksize)
    {
        $this->blocksize = $blocksize;
    }

	/**
     * @param string $mode
     */
    public function setMode($mode)
    {
        $this->mode = $mode;
    }

	/**
     * This function decrypts the final response and returns it to the merchant.
     * @return string $decryptResponse
     */
    public function getResponsePayload()
    {
        try {
            $responseParams = array(
                'pRes' => $this->responsePayload,
                'pEncKey' => $this->key,
                'pEncIv' => $this->iv
            );
   
            $errorResponse = $this->validateResponseParam($responseParams);

            if ($errorResponse) {
                return $errorResponse;
            }

            $aesObj = new AES($this->responsePayload, $this->key, $this->blocksize, $this->mode, $this->iv);
            $aesObj->require_pkcs5();
            $decryptResponse = trim(preg_replace('/[\x00-\x1F\x7F]/', '', $aesObj->decrypt()));

            $implodedResp = explode("|", $decryptResponse);
            $hashCodeString = end($implodedResp);
            array_pop($implodedResp);

            $explodedHashValue = explode("=", $hashCodeString);
            $hashValue = trim($explodedHashValue[1]);

            $responseDataString = implode("|", $implodedResp);
            $generatedHash = sha1($responseDataString);

            if ($generatedHash == $hashValue) {
                return $decryptResponse;
            } else {
                return 'ERROR064';
            }

        } catch (Exception $ex) {
            echo "Exception In TransactionResposeBean :" . $ex->getMessage();
            return;
        }

        return "ERROR037";
    }
}
