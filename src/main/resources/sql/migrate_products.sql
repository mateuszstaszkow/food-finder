INSERT INTO food_finder.product (id, name, description, short_description, foodGroup_id)
SELECT /*+ MAX_EXECUTION_TIME(100000) */ DISTINCT
CAST(source_names.NDB_No AS UNSIGNED) AS id,
source_names.Shrt_Desc,
source_info.Footnt_Txt,
source_info2.Description,
CAST(source_names.FdGrp_Cd AS UNSIGNED)
FROM usda_national_nutrients.food_des AS source_names
INNER JOIN usda_national_nutrients.fd_group AS source_groups ON source_names.FdGrp_Cd=source_groups.FdGrp_Cd
LEFT JOIN usda_national_nutrients.footnote AS source_info ON source_names.NDB_No=source_info.NDB_No
LEFT JOIN usda_national_nutrients.langual ON source_names.NDB_No=usda_national_nutrients.langual.NDB_No
LEFT JOIN usda_national_nutrients.langdesc AS source_info2 ON usda_national_nutrients.langual.Factor_Code=source_info2.Factor_Code
INNER JOIN usda_national_nutrients.nut_data AS source_comp ON source_names.NDB_No=source_comp.NDB_No
INNER JOIN usda_national_nutrients.nutr_def AS source_comp_names ON source_comp.Nutr_No=source_comp_names.Nutr_No
WHERE CAST(source_names.NDB_No AS UNSIGNED) > (SELECT MAX(food_finder.product.id) FROM food_finder.product)
LIMIT 1;