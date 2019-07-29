\echo UXPROD-1735 - Start ----------------------------------------------------------------
\echo

update AMICUS.S_BIB1_SMNTC
set sql_whr = replace(sql_whr, '%s', '(%s)')
where sql_whr like '% IN \%s%';

update AMICUS.S_BIB1_SMNTC 
set sql_whr = replace(sql_whr, '''%s''', '(''%s'')')
where sql_whr like '% IN ''\%s''%';

\echo
\echo UXPROD-1735 - End ------------------------------------------------------------------