package com.x.file.assemble.control.jaxrs.share;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.bean.WrapCopier;
import com.x.base.core.project.bean.WrapCopierFactory;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.tools.SortTools;
import com.x.file.assemble.control.Business;
import com.x.file.core.entity.personal.Share;

import java.util.List;

class ActionListMyShare extends BaseAction {

	ActionResult<List<Wo>> execute(EffectivePerson effectivePerson, String shareType) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<List<Wo>> result = new ActionResult<>();
			Business business = new Business(emc);
			List<Wo> wos = Wo.copier.copy(business.share().listWithPerson(effectivePerson.getDistinguishedName(), shareType));
			for (Wo o : wos) {
				o.setContentType(this.contentType(false, o.getName()));
			}
			SortTools.desc(wos, false, "createTime");
			result.setData(wos);
			return result;
		}
	}

	public static class Wo extends Share {

		private static final long serialVersionUID = -531053101150157872L;

		static WrapCopier<Share, Wo> copier = WrapCopierFactory.wo(Share.class, Wo.class, null,
				JpaObject.FieldsInvisible);

		@FieldDescribe("文件类型")
		private String contentType;

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

	}
}