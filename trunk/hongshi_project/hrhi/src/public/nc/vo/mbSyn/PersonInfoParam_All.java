package nc.vo.mbSyn;


import com.seeyon.client.AccountServiceStub.*;
import com.seeyon.client.PersonServiceStub.PersonInfoParam_Simple;


/**
 * 示例，实体类在com.seeyon.client.PersonServiceStub中，此类为参考
 * @author Administrator
 *
 */
public class PersonInfoParam_All extends PersonInfoParam_Simple
			implements org.apache.axis2.databinding.ADBBean {
		/*
		 * This type was generated from the piece of schema that had name =
		 * PersonInfoParam_All Namespace URI =
		 * http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd
		 * Namespace Prefix = ns3
		 */

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		private static java.lang.String generatePrefix(
				java.lang.String namespace) {
			if (namespace
					.equals("http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd")) {
				return "ns3";
			}
			return org.apache.axis2.databinding.utils.BeanUtil
					.getUniquePrefix();
		}

		/**
		 * field for Birthday
		 */

		protected java.lang.String localBirthday;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localBirthdayTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getBirthday() {
			return localBirthday;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Birthday
		 */
		public void setBirthday(java.lang.String param) {

			if (param != null) {
				// update the setting tracker
				localBirthdayTracker = true;
			} else {
				localBirthdayTracker = true;

			}

			this.localBirthday = param;

		}

		/**
		 * field for DepartmentName This was an Array!
		 */

		protected java.lang.String[] localDepartmentName;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localDepartmentNameTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String[]
		 */
		public java.lang.String[] getDepartmentName() {
			return localDepartmentName;
		}

		/**
		 * validate the array for DepartmentName
		 */
		protected void validateDepartmentName(java.lang.String[] param) {

		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            DepartmentName
		 */
		public void setDepartmentName(java.lang.String[] param) {

			validateDepartmentName(param);

			if (param != null) {
				// update the setting tracker
				localDepartmentNameTracker = true;
			} else {
				localDepartmentNameTracker = true;

			}

			this.localDepartmentName = param;
		}

		/**
		 * Auto generated add method for the array for convenience
		 * 
		 * @param param
		 *            java.lang.String
		 */
		public void addDepartmentName(java.lang.String param) {
			if (localDepartmentName == null) {
				localDepartmentName = new java.lang.String[] {};
			}

			// update the setting tracker
			localDepartmentNameTracker = true;

			java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil
					.toList(localDepartmentName);
			list.add(param);
			this.localDepartmentName = (java.lang.String[]) list
					.toArray(new java.lang.String[list.size()]);

		}

		/**
		 * field for Discursion
		 */

		protected java.lang.String localDiscursion;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localDiscursionTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getDiscursion() {
			return localDiscursion;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Discursion
		 */
		public void setDiscursion(java.lang.String param) {

			if (param != null) {
				// update the setting tracker
				localDiscursionTracker = true;
			} else {
				localDiscursionTracker = true;

			}

			this.localDiscursion = param;

		}

		/**
		 * field for Email
		 */

		protected java.lang.String localEmail;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localEmailTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getEmail() {
			return localEmail;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Email
		 */
		public void setEmail(java.lang.String param) {

			if (param != null) {
				// update the setting tracker
				localEmailTracker = true;
			} else {
				localEmailTracker = true;

			}

			this.localEmail = param;

		}

		/**
		 * field for FamilyAddress
		 */

		protected java.lang.String localFamilyAddress;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localFamilyAddressTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getFamilyAddress() {
			return localFamilyAddress;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            FamilyAddress
		 */
		public void setFamilyAddress(java.lang.String param) {

			if (param != null) {
				// update the setting tracker
				localFamilyAddressTracker = true;
			} else {
				localFamilyAddressTracker = true;

			}

			this.localFamilyAddress = param;

		}

		/**
		 * field for FamilyPhone
		 */

		protected java.lang.String localFamilyPhone;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localFamilyPhoneTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getFamilyPhone() {
			return localFamilyPhone;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            FamilyPhone
		 */
		public void setFamilyPhone(java.lang.String param) {

			if (param != null) {
				// update the setting tracker
				localFamilyPhoneTracker = true;
			} else {
				localFamilyPhoneTracker = true;

			}

			this.localFamilyPhone = param;

		}

		/**
		 * field for Identity
		 */

		protected java.lang.String localIdentity;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localIdentityTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getIdentity() {
			return localIdentity;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Identity
		 */
		public void setIdentity(java.lang.String param) {

			if (param != null) {
				// update the setting tracker
				localIdentityTracker = true;
			} else {
				localIdentityTracker = true;

			}

			this.localIdentity = param;

		}

		/**
		 * field for MobilePhone
		 */

		protected java.lang.String localMobilePhone;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localMobilePhoneTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getMobilePhone() {
			return localMobilePhone;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            MobilePhone
		 */
		public void setMobilePhone(java.lang.String param) {

			if (param != null) {
				// update the setting tracker
				localMobilePhoneTracker = true;
			} else {
				localMobilePhoneTracker = true;

			}

			this.localMobilePhone = param;

		}

		/**
		 * field for OcupationName
		 */

		protected java.lang.String localOcupationName;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localOcupationNameTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getOcupationName() {
			return localOcupationName;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            OcupationName
		 */
		public void setOcupationName(java.lang.String param) {

			if (param != null) {
				// update the setting tracker
				localOcupationNameTracker = true;
			} else {
				localOcupationNameTracker = true;

			}

			this.localOcupationName = param;

		}

		/**
		 * field for OfficePhone
		 */

		protected java.lang.String localOfficePhone;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localOfficePhoneTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getOfficePhone() {
			return localOfficePhone;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            OfficePhone
		 */
		public void setOfficePhone(java.lang.String param) {

			if (param != null) {
				// update the setting tracker
				localOfficePhoneTracker = true;
			} else {
				localOfficePhoneTracker = true;

			}

			this.localOfficePhone = param;

		}

		/**
		 * field for OtypeName
		 */

		protected java.lang.String localOtypeName;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localOtypeNameTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getOtypeName() {
			return localOtypeName;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            OtypeName
		 */
		public void setOtypeName(java.lang.String param) {

			if (param != null) {
				// update the setting tracker
				localOtypeNameTracker = true;
			} else {
				localOtypeNameTracker = true;

			}

			this.localOtypeName = param;

		}

		/**
		 * field for Per_sort
		 */

		protected java.lang.String localPer_sort;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localPer_sortTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getPer_sort() {
			return localPer_sort;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Per_sort
		 */
		public void setPer_sort(java.lang.String param) {

			if (param != null) {
				// update the setting tracker
				localPer_sortTracker = true;
			} else {
				localPer_sortTracker = true;

			}

			this.localPer_sort = param;

		}

		/**
		 * field for PersonType
		 */

		protected int localPersonType;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localPersonTypeTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return int
		 */
		public int getPersonType() {
			return localPersonType;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            PersonType
		 */
		public void setPersonType(int param) {

			// setting primitive attribute tracker to true

			if (param == java.lang.Integer.MIN_VALUE) {
				localPersonTypeTracker = false;

			} else {
				localPersonTypeTracker = true;
			}

			this.localPersonType = param;

		}

		/**
		 * field for SecondOcupationName This was an Array!
		 */

		protected java.lang.String[] localSecondOcupationName;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localSecondOcupationNameTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String[]
		 */
		public java.lang.String[] getSecondOcupationName() {
			return localSecondOcupationName;
		}

		/**
		 * validate the array for SecondOcupationName
		 */
		protected void validateSecondOcupationName(java.lang.String[] param) {

		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            SecondOcupationName
		 */
		public void setSecondOcupationName(java.lang.String[] param) {

			validateSecondOcupationName(param);

			if (param != null) {
				// update the setting tracker
				localSecondOcupationNameTracker = true;
			} else {
				localSecondOcupationNameTracker = true;

			}

			this.localSecondOcupationName = param;
		}

		/**
		 * Auto generated add method for the array for convenience
		 * 
		 * @param param
		 *            java.lang.String
		 */
		public void addSecondOcupationName(java.lang.String param) {
			if (localSecondOcupationName == null) {
				localSecondOcupationName = new java.lang.String[] {};
			}

			// update the setting tracker
			localSecondOcupationNameTracker = true;

			java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil
					.toList(localSecondOcupationName);
			list.add(param);
			this.localSecondOcupationName = (java.lang.String[]) list
					.toArray(new java.lang.String[list.size()]);

		}

		/**
		 * field for Sex
		 */

		protected java.lang.String localSex;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localSexTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getSex() {
			return localSex;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            Sex
		 */
		public void setSex(java.lang.String param) {

			if (param != null) {
				// update the setting tracker
				localSexTracker = true;
			} else {
				localSexTracker = true;

			}

			this.localSex = param;

		}

		/**
		 * field for StaffNumber
		 */

		protected java.lang.String localStaffNumber;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localStaffNumberTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getStaffNumber() {
			return localStaffNumber;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            StaffNumber
		 */
		public void setStaffNumber(java.lang.String param) {

			if (param != null) {
				// update the setting tracker
				localStaffNumberTracker = true;
			} else {
				localStaffNumberTracker = true;

			}

			this.localStaffNumber = param;

		}

		/**
		 * field for TrueName
		 */

		protected java.lang.String localTrueName;

		/*
		 * This tracker boolean wil be used to detect whether the user called
		 * the set method for this attribute. It will be used to determine
		 * whether to include this field in the serialized XML
		 */
		protected boolean localTrueNameTracker = false;

		/**
		 * Auto generated getter method
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getTrueName() {
			return localTrueName;
		}

		/**
		 * Auto generated setter method
		 * 
		 * @param param
		 *            TrueName
		 */
		public void setTrueName(java.lang.String param) {

			if (param != null) {
				// update the setting tracker
				localTrueNameTracker = true;
			} else {
				localTrueNameTracker = true;

			}

			this.localTrueName = param;

		}

		/**
		 * isReaderMTOMAware
		 * 
		 * @return true if the reader supports MTOM
		 */
		public static boolean isReaderMTOMAware(
				javax.xml.stream.XMLStreamReader reader) {
			boolean isReaderMTOMAware = false;

			try {
				isReaderMTOMAware = java.lang.Boolean.TRUE
						.equals(reader
								.getProperty(org.apache.axiom.om.OMConstants.IS_DATA_HANDLERS_AWARE));
			} catch (java.lang.IllegalArgumentException e) {
				isReaderMTOMAware = false;
			}
			return isReaderMTOMAware;
		}

		/**
		 * 
		 * @param parentQName
		 * @param factory
		 * @return org.apache.axiom.om.OMElement
		 */
		public org.apache.axiom.om.OMElement getOMElement(
				final javax.xml.namespace.QName parentQName,
				final org.apache.axiom.om.OMFactory factory)
				throws org.apache.axis2.databinding.ADBException {

			org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(
					this, parentQName) {

				public void serialize(
						org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
						throws javax.xml.stream.XMLStreamException {
					PersonInfoParam_All.this.serialize(parentQName, factory,
							xmlWriter);
				}
			};
			return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(
					parentQName, factory, dataSource);

		}

		public void serialize(
				final javax.xml.namespace.QName parentQName,
				final org.apache.axiom.om.OMFactory factory,
				org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {
			serialize(parentQName, factory, xmlWriter, false);
		}

		public void serialize(
				final javax.xml.namespace.QName parentQName,
				final org.apache.axiom.om.OMFactory factory,
				org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter,
				boolean serializeType)
				throws javax.xml.stream.XMLStreamException,
				org.apache.axis2.databinding.ADBException {

			java.lang.String prefix = null;
			java.lang.String namespace = null;

			prefix = parentQName.getPrefix();
			namespace = parentQName.getNamespaceURI();

			if ((namespace != null) && (namespace.trim().length() > 0)) {
				java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
				if (writerPrefix != null) {
					xmlWriter.writeStartElement(namespace,
							parentQName.getLocalPart());
				} else {
					if (prefix == null) {
						prefix = generatePrefix(namespace);
					}

					xmlWriter.writeStartElement(prefix,
							parentQName.getLocalPart(), namespace);
					xmlWriter.writeNamespace(prefix, namespace);
					xmlWriter.setPrefix(prefix, namespace);
				}
			} else {
				xmlWriter.writeStartElement(parentQName.getLocalPart());
			}

			java.lang.String namespacePrefix = registerPrefix(xmlWriter,
					"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd");
			if ((namespacePrefix != null)
					&& (namespacePrefix.trim().length() > 0)) {
				writeAttribute("xsi",
						"http://www.w3.org/2001/XMLSchema-instance", "type",
						namespacePrefix + ":PersonInfoParam_All", xmlWriter);
			} else {
				writeAttribute("xsi",
						"http://www.w3.org/2001/XMLSchema-instance", "type",
						"PersonInfoParam_All", xmlWriter);
			}

			if (localTypeNameTracker) {
				namespace = "http://common.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "typeName",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "typeName");
					}

				} else {
					xmlWriter.writeStartElement("typeName");
				}

				if (localTypeName == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localTypeName);

				}

				xmlWriter.writeEndElement();
			}
			if (localVersionTracker) {
				namespace = "http://common.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "version",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "version");
					}

				} else {
					xmlWriter.writeStartElement("version");
				}

				if (localVersion == java.lang.Integer.MIN_VALUE) {

					throw new org.apache.axis2.databinding.ADBException(
							"version cannot be null!!");

				} else {
					xmlWriter
							.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(localVersion));
				}

				xmlWriter.writeEndElement();
			}
			if (localAccountIdTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "accountId",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "accountId");
					}

				} else {
					xmlWriter.writeStartElement("accountId");
				}

				if (localAccountId == java.lang.Long.MIN_VALUE) {

					throw new org.apache.axis2.databinding.ADBException(
							"accountId cannot be null!!");

				} else {
					xmlWriter
							.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(localAccountId));
				}

				xmlWriter.writeEndElement();
			}
			if (localLoginNameTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "loginName",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "loginName");
					}

				} else {
					xmlWriter.writeStartElement("loginName");
				}

				if (localLoginName == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localLoginName);

				}

				xmlWriter.writeEndElement();
			}
			if (localPassWordTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "passWord",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "passWord");
					}

				} else {
					xmlWriter.writeStartElement("passWord");
				}

				if (localPassWord == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localPassWord);

				}

				xmlWriter.writeEndElement();
			}
			if (localPersonTypeTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "personType",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "personType");
					}

				} else {
					xmlWriter.writeStartElement("personType");
				}

				if (localPersonType == java.lang.Integer.MIN_VALUE) {

					throw new org.apache.axis2.databinding.ADBException(
							"personType cannot be null!!");

				} else {
					xmlWriter
							.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(localPersonType));
				}

				xmlWriter.writeEndElement();
			}
			if (localBirthdayTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "birthday",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "birthday");
					}

				} else {
					xmlWriter.writeStartElement("birthday");
				}

				if (localBirthday == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localBirthday);

				}

				xmlWriter.writeEndElement();
			}
			if (localDepartmentNameTracker) {
				if (localDepartmentName != null) {
					namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
					boolean emptyNamespace = namespace == null
							|| namespace.length() == 0;
					prefix = emptyNamespace ? null : xmlWriter
							.getPrefix(namespace);
					for (int i = 0; i < localDepartmentName.length; i++) {

						if (localDepartmentName[i] != null) {

							if (!emptyNamespace) {
								if (prefix == null) {
									java.lang.String prefix2 = generatePrefix(namespace);

									xmlWriter.writeStartElement(prefix2,
											"departmentName", namespace);
									xmlWriter
											.writeNamespace(prefix2, namespace);
									xmlWriter.setPrefix(prefix2, namespace);

								} else {
									xmlWriter.writeStartElement(namespace,
											"departmentName");
								}

							} else {
								xmlWriter.writeStartElement("departmentName");
							}

							xmlWriter
									.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(localDepartmentName[i]));

							xmlWriter.writeEndElement();

						} else {

							// write null attribute
							namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
							if (!namespace.equals("")) {
								prefix = xmlWriter.getPrefix(namespace);

								if (prefix == null) {
									prefix = generatePrefix(namespace);

									xmlWriter.writeStartElement(prefix,
											"departmentName", namespace);
									xmlWriter.writeNamespace(prefix, namespace);
									xmlWriter.setPrefix(prefix, namespace);

								} else {
									xmlWriter.writeStartElement(namespace,
											"departmentName");
								}

							} else {
								xmlWriter.writeStartElement("departmentName");
							}
							writeAttribute(
									"xsi",
									"http://www.w3.org/2001/XMLSchema-instance",
									"nil", "1", xmlWriter);
							xmlWriter.writeEndElement();

						}

					}
				} else {

					// write the null attribute
					// write null attribute
					java.lang.String namespace2 = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
					if (!namespace2.equals("")) {
						java.lang.String prefix2 = xmlWriter
								.getPrefix(namespace2);

						if (prefix2 == null) {
							prefix2 = generatePrefix(namespace2);

							xmlWriter.writeStartElement(prefix2,
									"departmentName", namespace2);
							xmlWriter.writeNamespace(prefix2, namespace2);
							xmlWriter.setPrefix(prefix2, namespace2);

						} else {
							xmlWriter.writeStartElement(namespace2,
									"departmentName");
						}

					} else {
						xmlWriter.writeStartElement("departmentName");
					}

					// write the nil attribute
					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);
					xmlWriter.writeEndElement();

				}

			}
			if (localDiscursionTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "discursion",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "discursion");
					}

				} else {
					xmlWriter.writeStartElement("discursion");
				}

				if (localDiscursion == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localDiscursion);

				}

				xmlWriter.writeEndElement();
			}
			if (localEmailTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "email", namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "email");
					}

				} else {
					xmlWriter.writeStartElement("email");
				}

				if (localEmail == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localEmail);

				}

				xmlWriter.writeEndElement();
			}
			if (localFamilyAddressTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "familyAddress",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "familyAddress");
					}

				} else {
					xmlWriter.writeStartElement("familyAddress");
				}

				if (localFamilyAddress == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localFamilyAddress);

				}

				xmlWriter.writeEndElement();
			}
			if (localFamilyPhoneTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "familyPhone",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "familyPhone");
					}

				} else {
					xmlWriter.writeStartElement("familyPhone");
				}

				if (localFamilyPhone == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localFamilyPhone);

				}

				xmlWriter.writeEndElement();
			}
			if (localIdentityTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "identity",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "identity");
					}

				} else {
					xmlWriter.writeStartElement("identity");
				}

				if (localIdentity == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localIdentity);

				}

				xmlWriter.writeEndElement();
			}
			if (localMobilePhoneTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "mobilePhone",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "mobilePhone");
					}

				} else {
					xmlWriter.writeStartElement("mobilePhone");
				}

				if (localMobilePhone == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localMobilePhone);

				}

				xmlWriter.writeEndElement();
			}
			if (localOcupationNameTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "ocupationName",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "ocupationName");
					}

				} else {
					xmlWriter.writeStartElement("ocupationName");
				}

				if (localOcupationName == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localOcupationName);

				}

				xmlWriter.writeEndElement();
			}
			if (localOfficePhoneTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "officePhone",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "officePhone");
					}

				} else {
					xmlWriter.writeStartElement("officePhone");
				}

				if (localOfficePhone == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localOfficePhone);

				}

				xmlWriter.writeEndElement();
			}
			if (localOtypeNameTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "otypeName",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "otypeName");
					}

				} else {
					xmlWriter.writeStartElement("otypeName");
				}

				if (localOtypeName == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localOtypeName);

				}

				xmlWriter.writeEndElement();
			}
			if (localPer_sortTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "per_sort",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "per_sort");
					}

				} else {
					xmlWriter.writeStartElement("per_sort");
				}

				if (localPer_sort == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localPer_sort);

				}

				xmlWriter.writeEndElement();
			}
			if (localPersonTypeTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "personType",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "personType");
					}

				} else {
					xmlWriter.writeStartElement("personType");
				}

				if (localPersonType == java.lang.Integer.MIN_VALUE) {

					throw new org.apache.axis2.databinding.ADBException(
							"personType cannot be null!!");

				} else {
					xmlWriter
							.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(localPersonType));
				}

				xmlWriter.writeEndElement();
			}
			if (localSecondOcupationNameTracker) {
				if (localSecondOcupationName != null) {
					namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
					boolean emptyNamespace = namespace == null
							|| namespace.length() == 0;
					prefix = emptyNamespace ? null : xmlWriter
							.getPrefix(namespace);
					for (int i = 0; i < localSecondOcupationName.length; i++) {

						if (localSecondOcupationName[i] != null) {

							if (!emptyNamespace) {
								if (prefix == null) {
									java.lang.String prefix2 = generatePrefix(namespace);

									xmlWriter.writeStartElement(prefix2,
											"secondOcupationName", namespace);
									xmlWriter
											.writeNamespace(prefix2, namespace);
									xmlWriter.setPrefix(prefix2, namespace);

								} else {
									xmlWriter.writeStartElement(namespace,
											"secondOcupationName");
								}

							} else {
								xmlWriter
										.writeStartElement("secondOcupationName");
							}

							xmlWriter
									.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(localSecondOcupationName[i]));

							xmlWriter.writeEndElement();

						} else {

							// write null attribute
							namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
							if (!namespace.equals("")) {
								prefix = xmlWriter.getPrefix(namespace);

								if (prefix == null) {
									prefix = generatePrefix(namespace);

									xmlWriter.writeStartElement(prefix,
											"secondOcupationName", namespace);
									xmlWriter.writeNamespace(prefix, namespace);
									xmlWriter.setPrefix(prefix, namespace);

								} else {
									xmlWriter.writeStartElement(namespace,
											"secondOcupationName");
								}

							} else {
								xmlWriter
										.writeStartElement("secondOcupationName");
							}
							writeAttribute(
									"xsi",
									"http://www.w3.org/2001/XMLSchema-instance",
									"nil", "1", xmlWriter);
							xmlWriter.writeEndElement();

						}

					}
				} else {

					// write the null attribute
					// write null attribute
					java.lang.String namespace2 = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
					if (!namespace2.equals("")) {
						java.lang.String prefix2 = xmlWriter
								.getPrefix(namespace2);

						if (prefix2 == null) {
							prefix2 = generatePrefix(namespace2);

							xmlWriter.writeStartElement(prefix2,
									"secondOcupationName", namespace2);
							xmlWriter.writeNamespace(prefix2, namespace2);
							xmlWriter.setPrefix(prefix2, namespace2);

						} else {
							xmlWriter.writeStartElement(namespace2,
									"secondOcupationName");
						}

					} else {
						xmlWriter.writeStartElement("secondOcupationName");
					}

					// write the nil attribute
					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);
					xmlWriter.writeEndElement();

				}

			}
			if (localSexTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "sex", namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "sex");
					}

				} else {
					xmlWriter.writeStartElement("sex");
				}

				if (localSex == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localSex);

				}

				xmlWriter.writeEndElement();
			}
			if (localStaffNumberTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "staffNumber",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "staffNumber");
					}

				} else {
					xmlWriter.writeStartElement("staffNumber");
				}

				if (localStaffNumber == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localStaffNumber);

				}

				xmlWriter.writeEndElement();
			}
			if (localTrueNameTracker) {
				namespace = "http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd";
				if (!namespace.equals("")) {
					prefix = xmlWriter.getPrefix(namespace);

					if (prefix == null) {
						prefix = generatePrefix(namespace);

						xmlWriter.writeStartElement(prefix, "trueName",
								namespace);
						xmlWriter.writeNamespace(prefix, namespace);
						xmlWriter.setPrefix(prefix, namespace);

					} else {
						xmlWriter.writeStartElement(namespace, "trueName");
					}

				} else {
					xmlWriter.writeStartElement("trueName");
				}

				if (localTrueName == null) {
					// write the nil attribute

					writeAttribute("xsi",
							"http://www.w3.org/2001/XMLSchema-instance", "nil",
							"1", xmlWriter);

				} else {

					xmlWriter.writeCharacters(localTrueName);

				}

				xmlWriter.writeEndElement();
			}
			xmlWriter.writeEndElement();

		}

		/**
		 * Util method to write an attribute with the ns prefix
		 */
		private void writeAttribute(java.lang.String prefix,
				java.lang.String namespace, java.lang.String attName,
				java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (xmlWriter.getPrefix(namespace) == null) {
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);

			}

			xmlWriter.writeAttribute(namespace, attName, attValue);

		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeAttribute(java.lang.String namespace,
				java.lang.String attName, java.lang.String attValue,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attValue);
			}
		}

		/**
		 * Util method to write an attribute without the ns prefix
		 */
		private void writeQNameAttribute(java.lang.String namespace,
				java.lang.String attName, javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			java.lang.String attributeNamespace = qname.getNamespaceURI();
			java.lang.String attributePrefix = xmlWriter
					.getPrefix(attributeNamespace);
			if (attributePrefix == null) {
				attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
			}
			java.lang.String attributeValue;
			if (attributePrefix.trim().length() > 0) {
				attributeValue = attributePrefix + ":" + qname.getLocalPart();
			} else {
				attributeValue = qname.getLocalPart();
			}

			if (namespace.equals("")) {
				xmlWriter.writeAttribute(attName, attributeValue);
			} else {
				registerPrefix(xmlWriter, namespace);
				xmlWriter.writeAttribute(namespace, attName, attributeValue);
			}
		}

		/**
		 * method to handle Qnames
		 */

		private void writeQName(javax.xml.namespace.QName qname,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String namespaceURI = qname.getNamespaceURI();
			if (namespaceURI != null) {
				java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
				if (prefix == null) {
					prefix = generatePrefix(namespaceURI);
					xmlWriter.writeNamespace(prefix, namespaceURI);
					xmlWriter.setPrefix(prefix, namespaceURI);
				}

				if (prefix.trim().length() > 0) {
					xmlWriter.writeCharacters(prefix
							+ ":"
							+ org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				} else {
					// i.e this is the default namespace
					xmlWriter
							.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(qname));
				}

			} else {
				xmlWriter
						.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(qname));
			}
		}

		private void writeQNames(javax.xml.namespace.QName[] qnames,
				javax.xml.stream.XMLStreamWriter xmlWriter)
				throws javax.xml.stream.XMLStreamException {

			if (qnames != null) {
				// we have to store this data until last moment since it is not
				// possible to write any
				// namespace data after writing the charactor data
				java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
				java.lang.String namespaceURI = null;
				java.lang.String prefix = null;

				for (int i = 0; i < qnames.length; i++) {
					if (i > 0) {
						stringToWrite.append(" ");
					}
					namespaceURI = qnames[i].getNamespaceURI();
					if (namespaceURI != null) {
						prefix = xmlWriter.getPrefix(namespaceURI);
						if ((prefix == null) || (prefix.length() == 0)) {
							prefix = generatePrefix(namespaceURI);
							xmlWriter.writeNamespace(prefix, namespaceURI);
							xmlWriter.setPrefix(prefix, namespaceURI);
						}

						if (prefix.trim().length() > 0) {
							stringToWrite
									.append(prefix)
									.append(":")
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						} else {
							stringToWrite
									.append(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(qnames[i]));
						}
					} else {
						stringToWrite
								.append(org.apache.axis2.databinding.utils.ConverterUtil
										.convertToString(qnames[i]));
					}
				}
				xmlWriter.writeCharacters(stringToWrite.toString());
			}

		}

		/**
		 * Register a namespace prefix
		 */
		private java.lang.String registerPrefix(
				javax.xml.stream.XMLStreamWriter xmlWriter,
				java.lang.String namespace)
				throws javax.xml.stream.XMLStreamException {
			java.lang.String prefix = xmlWriter.getPrefix(namespace);

			if (prefix == null) {
				prefix = generatePrefix(namespace);

				while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
					prefix = org.apache.axis2.databinding.utils.BeanUtil
							.getUniquePrefix();
				}

				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			}

			return prefix;
		}

		/**
		 * databinding method to get an XML representation of this object
		 * 
		 */
		public javax.xml.stream.XMLStreamReader getPullParser(
				javax.xml.namespace.QName qName)
				throws org.apache.axis2.databinding.ADBException {

			java.util.ArrayList elementList = new java.util.ArrayList();
			java.util.ArrayList attribList = new java.util.ArrayList();

			attribList.add(new javax.xml.namespace.QName(
					"http://www.w3.org/2001/XMLSchema-instance", "type"));
			attribList
					.add(new javax.xml.namespace.QName(
							"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
							"PersonInfoParam_All"));
			if (localTypeNameTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://common.oainterface.seeyon.com/xsd",
								"typeName"));

				elementList.add(localTypeName == null ? null
						: org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localTypeName));
			}
			if (localVersionTracker) {
				elementList.add(new javax.xml.namespace.QName(
						"http://common.oainterface.seeyon.com/xsd", "version"));

				elementList
						.add(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localVersion));
			}
			if (localAccountIdTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"accountId"));

				elementList
						.add(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localAccountId));
			}
			if (localLoginNameTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"loginName"));

				elementList.add(localLoginName == null ? null
						: org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localLoginName));
			}
			if (localPassWordTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"passWord"));

				elementList.add(localPassWord == null ? null
						: org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localPassWord));
			}
			if (localPersonTypeTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"personType"));

				elementList
						.add(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localPersonType));
			}
			if (localBirthdayTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"birthday"));

				elementList.add(localBirthday == null ? null
						: org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localBirthday));
			}
			if (localDepartmentNameTracker) {
				if (localDepartmentName != null) {
					for (int i = 0; i < localDepartmentName.length; i++) {

						if (localDepartmentName[i] != null) {
							elementList
									.add(new javax.xml.namespace.QName(
											"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
											"departmentName"));
							elementList
									.add(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(localDepartmentName[i]));
						} else {

							elementList
									.add(new javax.xml.namespace.QName(
											"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
											"departmentName"));
							elementList.add(null);

						}

					}
				} else {

					elementList
							.add(new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"departmentName"));
					elementList.add(null);

				}

			}
			if (localDiscursionTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"discursion"));

				elementList.add(localDiscursion == null ? null
						: org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localDiscursion));
			}
			if (localEmailTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"email"));

				elementList.add(localEmail == null ? null
						: org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localEmail));
			}
			if (localFamilyAddressTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"familyAddress"));

				elementList.add(localFamilyAddress == null ? null
						: org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localFamilyAddress));
			}
			if (localFamilyPhoneTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"familyPhone"));

				elementList.add(localFamilyPhone == null ? null
						: org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localFamilyPhone));
			}
			if (localIdentityTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"identity"));

				elementList.add(localIdentity == null ? null
						: org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localIdentity));
			}
			if (localMobilePhoneTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"mobilePhone"));

				elementList.add(localMobilePhone == null ? null
						: org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localMobilePhone));
			}
			if (localOcupationNameTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"ocupationName"));

				elementList.add(localOcupationName == null ? null
						: org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localOcupationName));
			}
			if (localOfficePhoneTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"officePhone"));

				elementList.add(localOfficePhone == null ? null
						: org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localOfficePhone));
			}
			if (localOtypeNameTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"otypeName"));

				elementList.add(localOtypeName == null ? null
						: org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localOtypeName));
			}
			if (localPer_sortTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"per_sort"));

				elementList.add(localPer_sort == null ? null
						: org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localPer_sort));
			}
			if (localPersonTypeTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"personType"));

				elementList
						.add(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localPersonType));
			}
			if (localSecondOcupationNameTracker) {
				if (localSecondOcupationName != null) {
					for (int i = 0; i < localSecondOcupationName.length; i++) {

						if (localSecondOcupationName[i] != null) {
							elementList
									.add(new javax.xml.namespace.QName(
											"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
											"secondOcupationName"));
							elementList
									.add(org.apache.axis2.databinding.utils.ConverterUtil
											.convertToString(localSecondOcupationName[i]));
						} else {

							elementList
									.add(new javax.xml.namespace.QName(
											"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
											"secondOcupationName"));
							elementList.add(null);

						}

					}
				} else {

					elementList
							.add(new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"secondOcupationName"));
					elementList.add(null);

				}

			}
			if (localSexTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"sex"));

				elementList.add(localSex == null ? null
						: org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localSex));
			}
			if (localStaffNumberTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"staffNumber"));

				elementList.add(localStaffNumber == null ? null
						: org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localStaffNumber));
			}
			if (localTrueNameTracker) {
				elementList
						.add(new javax.xml.namespace.QName(
								"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
								"trueName"));

				elementList.add(localTrueName == null ? null
						: org.apache.axis2.databinding.utils.ConverterUtil
								.convertToString(localTrueName));
			}

			return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(
					qName, elementList.toArray(), attribList.toArray());

		}

		/**
		 * Factory class that keeps the parse method
		 */
		public static class Factory {

			/**
			 * static method to create the object Precondition: If this object
			 * is an element, the current or next start element starts this
			 * object and any intervening reader events are ignorable If this
			 * object is not an element, it is a complex type and the reader is
			 * at the event just after the outer start element Postcondition: If
			 * this object is an element, the reader is positioned at its end
			 * element If this object is a complex type, the reader is
			 * positioned at the end element of its outer element
			 */
			public static PersonInfoParam_All parse(
					javax.xml.stream.XMLStreamReader reader)
					throws java.lang.Exception {
				PersonInfoParam_All object = new PersonInfoParam_All();

				int event;
				java.lang.String nillableValue = null;
				java.lang.String prefix = "";
				java.lang.String namespaceuri = "";
				try {

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader
							.getAttributeValue(
									"http://www.w3.org/2001/XMLSchema-instance",
									"type") != null) {
						java.lang.String fullTypeName = reader
								.getAttributeValue(
										"http://www.w3.org/2001/XMLSchema-instance",
										"type");
						if (fullTypeName != null) {
							java.lang.String nsPrefix = null;
							if (fullTypeName.indexOf(":") > -1) {
								nsPrefix = fullTypeName.substring(0,
										fullTypeName.indexOf(":"));
							}
							nsPrefix = nsPrefix == null ? "" : nsPrefix;

							java.lang.String type = fullTypeName
									.substring(fullTypeName.indexOf(":") + 1);

							if (!"PersonInfoParam_All".equals(type)) {
								// find namespace for the prefix
								java.lang.String nsUri = reader
										.getNamespaceContext().getNamespaceURI(
												nsPrefix);
								return (PersonInfoParam_All) ExtensionMapper
										.getTypeObject(nsUri, type, reader);
							}

						}

					}

					// Note all attributes that were handled. Used to differ
					// normal attributes
					// from anyAttributes.
					java.util.Vector handledAttributes = new java.util.Vector();

					reader.next();

					java.util.ArrayList list8 = new java.util.ArrayList();

					java.util.ArrayList list20 = new java.util.ArrayList();

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://common.oainterface.seeyon.com/xsd",
									"typeName").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							java.lang.String content = reader.getElementText();

							object.setTypeName(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes if
														// any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://common.oainterface.seeyon.com/xsd",
									"version").equals(reader.getName())) {

						java.lang.String content = reader.getElementText();

						object.setVersion(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToInt(content));

						reader.next();

					} // End of if for expected property start element

					else {

						object.setVersion(java.lang.Integer.MIN_VALUE);

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"accountId").equals(reader.getName())) {

						java.lang.String content = reader.getElementText();

						object.setAccountId(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToLong(content));

						reader.next();

					} // End of if for expected property start element

					else {

						object.setAccountId(java.lang.Long.MIN_VALUE);

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"loginName").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							java.lang.String content = reader.getElementText();

							object.setLoginName(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes if
														// any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"passWord").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							java.lang.String content = reader.getElementText();

							object.setPassWord(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes if
														// any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"personType").equals(reader.getName())) {

						java.lang.String content = reader.getElementText();

						object.setPersonType(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToInt(content));

						reader.next();

					} // End of if for expected property start element

					else {

						object.setPersonType(java.lang.Integer.MIN_VALUE);

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"birthday").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							java.lang.String content = reader.getElementText();

							object.setBirthday(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes if
														// any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"departmentName").equals(reader.getName())) {

						// Process the array and step past its final element's
						// end.

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if ("true".equals(nillableValue)
								|| "1".equals(nillableValue)) {
							list8.add(null);

							reader.next();
						} else {
							list8.add(reader.getElementText());
						}
						// loop until we find a start element that is not part
						// of this array
						boolean loopDone8 = false;
						while (!loopDone8) {
							// Ensure we are at the EndElement
							while (!reader.isEndElement()) {
								reader.next();
							}
							// Step out of this element
							reader.next();
							// Step to next element event.
							while (!reader.isStartElement()
									&& !reader.isEndElement())
								reader.next();
							if (reader.isEndElement()) {
								// two continuous end elements means we are
								// exiting the xml structure
								loopDone8 = true;
							} else {
								if (new javax.xml.namespace.QName(
										"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
										"departmentName").equals(reader
										.getName())) {

									nillableValue = reader
											.getAttributeValue(
													"http://www.w3.org/2001/XMLSchema-instance",
													"nil");
									if ("true".equals(nillableValue)
											|| "1".equals(nillableValue)) {
										list8.add(null);

										reader.next();
									} else {
										list8.add(reader.getElementText());
									}
								} else {
									loopDone8 = true;
								}
							}
						}
						// call the converter utility to convert and set the
						// array

						object.setDepartmentName((java.lang.String[]) list8
								.toArray(new java.lang.String[list8.size()]));

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"discursion").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							java.lang.String content = reader.getElementText();

							object.setDiscursion(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes if
														// any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"email").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							java.lang.String content = reader.getElementText();

							object.setEmail(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes if
														// any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"familyAddress").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							java.lang.String content = reader.getElementText();

							object.setFamilyAddress(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes if
														// any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"familyPhone").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							java.lang.String content = reader.getElementText();

							object.setFamilyPhone(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes if
														// any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"identity").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							java.lang.String content = reader.getElementText();

							object.setIdentity(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes if
														// any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"mobilePhone").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							java.lang.String content = reader.getElementText();

							object.setMobilePhone(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes if
														// any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"ocupationName").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							java.lang.String content = reader.getElementText();

							object.setOcupationName(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes if
														// any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"officePhone").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							java.lang.String content = reader.getElementText();

							object.setOfficePhone(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes if
														// any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"otypeName").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							java.lang.String content = reader.getElementText();

							object.setOtypeName(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes if
														// any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"per_sort").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							java.lang.String content = reader.getElementText();

							object.setPer_sort(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes if
														// any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"personType").equals(reader.getName())) {

						java.lang.String content = reader.getElementText();

						object.setPersonType(org.apache.axis2.databinding.utils.ConverterUtil
								.convertToInt(content));

						reader.next();

					} // End of if for expected property start element

					else {

						object.setPersonType(java.lang.Integer.MIN_VALUE);

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"secondOcupationName").equals(reader
									.getName())) {

						// Process the array and step past its final element's
						// end.

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if ("true".equals(nillableValue)
								|| "1".equals(nillableValue)) {
							list20.add(null);

							reader.next();
						} else {
							list20.add(reader.getElementText());
						}
						// loop until we find a start element that is not part
						// of this array
						boolean loopDone20 = false;
						while (!loopDone20) {
							// Ensure we are at the EndElement
							while (!reader.isEndElement()) {
								reader.next();
							}
							// Step out of this element
							reader.next();
							// Step to next element event.
							while (!reader.isStartElement()
									&& !reader.isEndElement())
								reader.next();
							if (reader.isEndElement()) {
								// two continuous end elements means we are
								// exiting the xml structure
								loopDone20 = true;
							} else {
								if (new javax.xml.namespace.QName(
										"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
										"secondOcupationName").equals(reader
										.getName())) {

									nillableValue = reader
											.getAttributeValue(
													"http://www.w3.org/2001/XMLSchema-instance",
													"nil");
									if ("true".equals(nillableValue)
											|| "1".equals(nillableValue)) {
										list20.add(null);

										reader.next();
									} else {
										list20.add(reader.getElementText());
									}
								} else {
									loopDone20 = true;
								}
							}
						}
						// call the converter utility to convert and set the
						// array

						object.setSecondOcupationName((java.lang.String[]) list20
								.toArray(new java.lang.String[list20.size()]));

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"sex").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							java.lang.String content = reader.getElementText();

							object.setSex(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes if
														// any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"staffNumber").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							java.lang.String content = reader.getElementText();

							object.setStaffNumber(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes if
														// any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement()
							&& new javax.xml.namespace.QName(
									"http://infoParamImpl.organizationmgr.oainterface.seeyon.com/xsd",
									"trueName").equals(reader.getName())) {

						nillableValue = reader.getAttributeValue(
								"http://www.w3.org/2001/XMLSchema-instance",
								"nil");
						if (!"true".equals(nillableValue)
								&& !"1".equals(nillableValue)) {

							java.lang.String content = reader.getElementText();

							object.setTrueName(org.apache.axis2.databinding.utils.ConverterUtil
									.convertToString(content));

						} else {

							reader.getElementText(); // throw away text nodes if
														// any.
						}

						reader.next();

					} // End of if for expected property start element

					else {

					}

					while (!reader.isStartElement() && !reader.isEndElement())
						reader.next();

					if (reader.isStartElement())
						// A start element we are not expecting indicates a
						// trailing invalid property
						throw new org.apache.axis2.databinding.ADBException(
								"Unexpected subelement "
										+ reader.getLocalName());

				} catch (javax.xml.stream.XMLStreamException e) {
					throw new java.lang.Exception(e);
				}

				return object;
			}

		}// end of factory class

	}