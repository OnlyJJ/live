package com.jiujun.shows.car.enums;

/**
 * 座驾赠送、购买历史
 *@author shao.xiang
 *@date 2017-06-19
 */
public class CarGiveHisEnum {
	
	/**
	 * 类型
	 *
	 */
	public static enum TypeEnum {
		
		/**
		 * 类型,0:购买,1:用户赠送,2:系统赠送(连续登陆7天),3:系统赠送(蜜桃礼物),4:系统赠送(充值赠送),5(其他),6:系统赠送(分享)
		 */
        buy(0), userGive(1), systemGive_Login7Days(2),systemGive_Peach(3),systemGive_Charge(4),other(5),userShare(6);

        //类型,0:购买,1:用户赠送,2:系统赠送',
        private   int type ;
        
        TypeEnum(int i){
        	this.type = i;
        }

		public int getType() {
			return type;
		}

    }

}
