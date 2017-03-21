* @ValidationCode : MjotNjExMjgwNjg6Q3AxMjUyOjE0ODg4MDI5NDU0NjI6aHNoYXNoYW5rOi0xOi0xOjA6MDpmYWxzZTpOL0E6REVWXzIwMTcwMS4wOi0xOi0x
* @ValidationInfo : Timestamp         : 06 Mar 2017 17:52:25
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
SUBROUTINE BNFC.VALIDATE
*-----------------------------------------------------------------------------
*
*-----------------------------------------------------------------------------
* Modification History :
*-----------------------------------------------------------------------------
*$INSERT I_COMMON
*$INSERT I_EQUATE
**-----------------------------------------------------------------------------
$USING EB.SystemTables
$USING AC.AccountOpening
$USING ST.Payments

BEN.ACCT.NO=EB.SystemTables.getComi()

R.ACC=AC.AccountOpening.Account.Read(BEN.ACCT.NO, Error)

CUST.NO=R.ACC<AC.AccountOpening.Account.Customer>

EB.SystemTables.setRNew(ST.Payments.Beneficiary.ArcBenBenCustomer,CUST.NO)


RETURN
