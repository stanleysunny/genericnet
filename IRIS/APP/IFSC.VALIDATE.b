* @ValidationCode : MjotMTk5MjEzMjE5NzpDcDEyNTI6MTQ4OTk4NTAyMDIzNjpoc2hhc2hhbms6LTE6LTE6MDowOmZhbHNlOk4vQTpERVZfMjAxNzAxLjA6LTE6LTE=
* @ValidationInfo : Timestamp         : 20 Mar 2017 10:13:40
* @ValidationInfo : Encoding          : Cp1252
* @ValidationInfo : User Name         : hshashank
* @ValidationInfo : Nb tests success  : N/A
* @ValidationInfo : Nb tests failure  : N/A
* @ValidationInfo : Rating            : N/A
* @ValidationInfo : Coverage          : N/A
* @ValidationInfo : Strict flag       : N/A
* @ValidationInfo : Bypass GateKeeper : false
* @ValidationInfo : Compiler Version  : DEV_201701.0
$PACKAGE EB.BNFC
SUBROUTINE IFSC.VALIDATE
*-----------------------------------------------------------------------------
*
*-----------------------------------------------------------------------------
* Modification History :
*-----------------------------------------------------------------------------
$USING EB.SystemTables
$USING FT.LocalClearing
$USING EB.ErrorProcessing

IFSC=EB.SystemTables.getComi()

IF IFSC NE '' THEN
	
	R.IFSC=FT.LocalClearing.BcSortCode.Read(IFSC, Error)
	
	IF R.IFSC EQ '' THEN
*	EB.SystemTables.setAf(ST.Payments.Beneficiary.ArcBenLocalRef)
	EB.SystemTables.setEtext('ST-IFSC-MISSING')
*	EB.ErrorProcessing.Err()
	END

END

RETURN
