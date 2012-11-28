select ehpta_calc_rebates_b.period , ehpta_calc_rebates_b.custname , ehpta_calc_rebates_b.concode , 
decode(ehpta_calc_rebates_b.def6 , null , 'x' , decode(ehpta_calc_rebates_b.def6 , 'N' , 'x' , 'Y' , '¡Ì')) def6 , ehpta_calc_rebates_b.num , ehpta_calc_rebates_b.nnumber , 
ehpta_calc_rebates_b.preprice , ehpta_calc_rebates_b.premny , ehpta_calc_rebates_b.def1 , 
ehpta_calc_rebates_b.def2 , ehpta_calc_rebates_b.def3 , ehpta_calc_rebates_b.def4 , ehpta_calc_rebates_b.actmny , 
decode(ehpta_adjust.pk_adjust , null , 'x' , '¡Ì') settleflag ,
decode(ehpta_adjust.pk_adjust , null , null , ehpta_adjust.adjustdate) settledate ,
operuser.user_name opername , appruser.user_name apprname
from ehpta_calc_rebates_b
left join ehpta_adjust on ehpta_adjust.def1 = ehpta_calc_rebates_b.pk_rebates_b
left join ehpta_calc_rebates_h on ehpta_calc_rebates_h.pk_rebates = ehpta_calc_rebates_b.pk_rebates
left join sm_user operuser on operuser.cuserid = ehpta_calc_rebates_h.voperatorid
left join sm_user appruser on appruser.cuserid = ehpta_calc_rebates_h.vapproveid
where ehpta_calc_rebates_h.vbillstatus = 1 and nvl(ehpta_calc_rebates_h.dr , 0) = 0
and nvl(ehpta_calc_rebates_b.dr , 0) = 0
