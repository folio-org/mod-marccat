update AMICUS.S_BIB1_SMNTC
set sql_whr = replace(sql_whr, '%s', '(%s)')
where sql_whr like '% IN \%s%';

update AMICUS.S_BIB1_SMNTC
set sql_whr = replace(sql_whr, '''%s''', '(''%s'')')
where sql_whr like '% IN ''\%s''%';
