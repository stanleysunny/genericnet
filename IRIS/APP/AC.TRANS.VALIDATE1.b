* @ValidationCode : MjotMTIwNjI4NzM5NjpDcDEyNTI6MTQ4ODg5MjQ5MTQ1ODpoc2hhc2hhbms6LTE6LTE6MDowOmZhbHNlOk4vQTpERVZfMjAxNzAxLjA6LTE6LTE=
* @ValidationInfo : Timestamp         : 07 Mar 2017 18:44:51
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
SUBROUTINE AC.TRANS.VALIDATE1
*-----------------------------------------------------------------------------
*
*-----------------------------------------------------------------------------
* Modification History :
*-----------------------------------------------------------------------------
$USING EB.SystemTables
$USING AC.AccountOpening
$USING FT.Contract

*DEBIT.ACCT.NO=EB.SystemTables.getRNew(FT.Contract.FundsTransfer.DebitAcctNo)
CREDIT.ACCT.NO=EB.SystemTables.getComi()

*R.DEBIT.ACC=AC.AccountOpening.Account.Read(DEBIT.ACCT.NO, Error)
R.CREDIT.ACC=AC.AccountOpening.Account.Read(CREDIT.ACCT.NO, Error)

*DEBIT.CURR=R.DEBIT.ACC<AC.AccountOpening.Account.Currency>
CREDIT.CURR=R.CREDIT.ACC<AC.AccountOpening.Account.Currency>

*EB.SystemTables.setRNew(FT.Contract.FundsTransfer.DebitCurrency,DEBIT.CURR)
EB.SystemTables.setRNew(FT.Contract.FundsTransfer.CreditCurrency,CREDIT.CURR)

RETURN
